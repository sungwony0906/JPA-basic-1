package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
		/**
		 * JPA 구동 방식
		 * 1. 설정 정보 조회 : Persistence 클래스에서 META-INF/persistence.xml 설정 정보를 조회한다
		 * 2. EntityManagerFactory 생성 : 설정 정보를 바탕으로 EntityManagerFactory 오브젝트를 생성한다
		 * 3. 생성 : EntityManagerFactory에서 EntityManager를 생성한다
		 **/


		// ** 주의 **
		// entityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유한다
		// entityManager는 쓰레드간에 공유하지 않는다 (사용하고 버려야 한다)
		// JPA의 모든 데이터 변경은 트랜잭션 안에서 실행된다.

		// persistence unit name은 persistence.xml에 지정한 값을 넘겨주어야 한다.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		// JPA의 작업 단위는 트랜잭션이다.
		// 트랜잭션이 없으면 Entity에 값을 설정해도 DB에 추가되지 않는다.
		EntityTransaction tx = em.getTransaction();

		// create
		System.out.println("========== create ==========");
		try {
			tx.begin();
			Member member = new Member();
			member.setId(1L);
			member.setName("HelloWorld");

			em.persist(member);

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		// read
		System.out.println("========== read ==========");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try{
			tx.begin();
			Member findMember = em.find(Member.class, 1L);
			System.out.println("findMember.id = "+findMember.getId());
			System.out.println("findMember.name = "+findMember.getName());

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		// update
		System.out.println("========== update ==========");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member findMember = em.find(Member.class, 1L);
			findMember.setName("ChangeWorld");
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		// delete
		System.out.println("========== delete ==========");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member findMember = em.find(Member.class, 1L);
			em.remove(findMember);
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		// JPQL

		// JPA를 사용하면 엔티티 객체를 중심으로 개발
		// 문제는 검색 쿼리
		// 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
		// 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
		// 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요

		// JPA는 SQL을 추상화한 JPQL 이라는 객체 지향 쿼리 언어 제공
		// SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
		// JPQL은 엔티티 객체를 대상으로 쿼리
		// SQL은 데이터베이스 테이블을 대상으로 쿼리
		System.out.println("========== JPQL ==========");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			List<Member> result = em.createQuery("select m from Member as m", Member.class)
									.setFirstResult(1)
									.setMaxResults(10)
									.getResultList();
			for(Member member : result){
				System.out.println("member.name = " + member.getName());
			}
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
