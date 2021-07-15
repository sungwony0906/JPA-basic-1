package proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import org.hibernate.Hibernate;

public class JpaMainProxy {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Member member1 = new Member();
			member1.setUsername("hello");
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("hello2");
			em.persist(member2);

			Member member3 = new Member();
			member3.setUsername("hello3");
			em.persist(member3);

			em.flush();
			em.clear();

			//실제 객체 대신 proxy를 가져온다
			//데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
			//프록시 객체는 실제 객체의 참조를 보관
			Member findMember = em.getReference(Member.class, member1.getId());
			System.out.println("findMember = "+findMember);
			System.out.println("findMember.id = "+findMember.getId());

			//타겟의 값이 필요할 때 데이터베이스 호출
			System.out.println("findMember.username = "+findMember.getUsername());

			//프록시 객체는 처음 사용할 때 한 번만 초기화
			//프록시 객체를 초기화 할 때 프록시 객체가 실제 엔티티로 바뀌는 것은 아니다. 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
			//프록시 객체는 원본 엔티티를 상속받는다, 따라서 타입 체크시 == 비교 실패, 대신 instance of를 사용

			Member m2 = em.find(Member.class, member2.getId()); // 데이터베이스를 통해서 실제 엔티티 객체 조회
			Member m3 = em.find(Member.class, member3.getId()); // 데이터베이스를 통해서 실제 엔티티 객체 조회

			System.out.println("m2 == m3 : "+(m2.getClass() == m3.getClass()));
			System.out.println("m2 == findMember : "+(m2.getClass() == findMember.getClass()));
			System.out.println("findMember instance of Member : "+(findMember instanceof Member));

			Team teamA = new Team();
			teamA.setName("teamA");
			em.persist(teamA);

			//영속성 컨텍스트에 이미 값이 있으면 em.getReference()를 호출해도 실제 엔티티를 반환
			Team team = em.find(Team.class, teamA.getId());
			Team entityTeam = em.getReference(Team.class, teamA.getId());
			System.out.println("team == entityTeam : "+(team.getClass() == entityTeam.getClass()));

			//프록시 객체를 가지고 있는 상태에서 준영속 상태가 된 경우
			Team teamB = new Team();
			teamB.setName("teamB");
			em.persist(teamB);

			Team team2 = em.getReference(Team.class, teamB.getId());
			em.detach(team2);

			team2.getName(); // 오류 발생

			//프록시 확인
			//인스턴스의 초기화 여부 확인
			System.out.println("isLoaded = "+emf.getPersistenceUnitUtil().isLoaded(team2));
			//프록시 클래스 확인
			System.out.println(team2.getClass().getName());
			//프록시 강제 초기화
			Hibernate.initialize(team2); //hibernate에만 존재

			tx.commit();
		} catch (Exception e){
			tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}

		emf.close();
	}
}
