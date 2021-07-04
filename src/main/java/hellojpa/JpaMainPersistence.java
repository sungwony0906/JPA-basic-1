package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMainPersistence {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		try {
			//비영속 -- 아무것도 아닌 상태
			Member member = new Member();
			member.setId(100L);
			member.setName("HelloJPA");

			//영속 -- DB에 저장되는 상태는 아니다
			System.out.println("=== BEFORE ===");
			em.persist(member);
			System.out.println("=== AFTER ===");

			//준영속 -- 엔티티를 영속성 컨텍스트에서 분리
			//em.detach(member);

			//실제 DB에 저장되는 시점은 트랜잭션을 커밋하는 시점
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member findMember = em.find(Member.class, 100L);
			//삭제
			em.remove(findMember);
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		// entity manager에 의한 캐싱
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member = new Member();
			member.setId(101L);
			member.setName("HelloJPA2");

			System.out.println("=== BEFORE ===");
			em.persist(member);
			System.out.println("=== AFTER ===");

			Member findMember = em.find(Member.class, 101L);
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();

			System.out.println("첫번째 조회");
			Member findMember1 = em.find(Member.class, 101L);
			System.out.println("두번째 조회");
			Member findMember2 = em.find(Member.class, 101L);

			System.out.println("result = "+(findMember1 == findMember2));
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		System.out.println("buffering");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member1 = new Member(150L, "A");
			Member member2 = new Member(160L, "B");

			em.persist(member1);
			em.persist(member2);

			System.out.println("=======================");

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		System.out.println("dirty checking");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member = em.find(Member.class, 150L);
			member.setName("ChangeName");

			//em.update(member); --> 이런 코드가 필요하지 않다.

			//엔티티와 스냅샷(값을 읽어온 시점의 객체의 상태)을 비교
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		System.out.println("flush");
		// flush 수행 시점
		// em.flush()
		// tx.commit()
		// JPQL 쿼리 실행

		// flush 모드
		// FlushModeType.AUTO - 커밋이나 쿼리를 실행할 떄 플러시 (기본값)
		// FlushModeType.COMMIT - 커밋할 때만 플러시

		// 영속성 컨텍스트를 비우지 않음
		// 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
		// 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됨

		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member1 = new Member(200L, "member200");
			em.persist(member1);

			em.flush();

			System.out.println("=======================");
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		System.out.println("detached");
		// em.detach(entity) - 특정 엔티티만 준영속으로
		// em.clear() - 영속성 컨텍스트를 비운다
		// em.close() - 영속성 컨텍스트를 종료
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member = em.find(Member.class, 150L);
			member.setName("AAAAA");

			em.detach(member); //JPA에서 관리하지 않겠다

			System.out.println("=======================");
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			Member member = em.find(Member.class, 150L);
			em.clear();
			Member member2 = em.find(Member.class, 150L);
			System.out.println("=======================");
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
