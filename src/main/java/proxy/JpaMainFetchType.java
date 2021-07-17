package proxy;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.hibernate.Hibernate;

public class JpaMainFetchType {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Member member1 = new Member();
			member1.setUsername("hello");
			em.persist(member1);

			Team team1 = new Team();
			team1.setName("team1");
			team1.addMember(member1);
			em.persist(team1);

			em.flush();
			em.clear();

			// JPQL로 Member 엔티티 만을 조회
			// Member.team -> FetchType.EAGLE
			// TEAM 조회
			// => N+1 쿼리가 발생 !
			List<Member> members = em.createQuery("select m from Member m", Member.class)
					.getResultList();

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
