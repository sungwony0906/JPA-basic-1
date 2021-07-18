package jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMainJPQL {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();

			Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			Member member2 = new Member();
			member2.setUsername("member2");
			member2.setAge(20);

			em.persist(member);
			em.persist(member2);

			//반환 타입이 명확할 때는 TypedQuery
			TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
			TypedQuery<Member> querySingle = em.createQuery("select m from Member m where m.id = 10", Member.class);

			//반환 타입이 명확하지 않을 때는 Query
			Query query1 = em.createQuery("select m.username, m.age from Member m");

			List<Member> resultList = query.getResultList();// 리스트 반환

			Member singleResult = querySingle.getSingleResult();// 결과가 정확히 하나여야 함
			//결과가 없으면 : javax.persistence.NoResultException
			//둘 이상이면 : javax.persistence.NonUniqueResultException

			TypedQuery<Member> queryWithParameter = em.createQuery("select m from Member m where m.username = :username", Member.class);
			queryWithParameter.setParameter("username", "member1");
			Member singleResult1 = query.getSingleResult();

			System.out.println(singleResult.getId()+","+singleResult.getAge());

			//프로젝션
			//SELECT 절에 조회할 대상을 지정하는 것

			//엔티티 프로젝션
			List<Member> findMembers = em.createQuery("select m from Member m", Member.class)
											   .getResultList();

			findMembers.get(0).setAge(20); //엔티티 프로젝션은 Update 쿼리가 나간다.
			List<Team> findTeams = em.createQuery("select m.team from Member m", Team.class) // 묵시적 Join으로 Team 엔티티를 조회한다
					.getResultList();

			//임베디드 타입 프로젝션
			List<Address> findAddressList = em.createQuery("select o.address from Order o",
					Address.class).getResultList();

			//스칼라 타입 프로젝션
			List resultList1 = em.createQuery("select distinct m.username, m.age from Member m")
									   .getResultList();

			//스칼라 타입 프로젝션의 경우 타입을 알 수 없어 Object 배열의 리스트로 반환
			Object[] result = (Object[]) resultList1.get(0);
			System.out.println("username = "+result[0]);
			System.out.println("age = "+result[1]);

			List<Object[]> resultList2 = em.createQuery("select distinct m.username, m.age from Member m")
									   .getResultList(); // 이런식으로도 받을 수 있다

			// DTO로 바로 받는 방법
			List<MemberDTO> resultList3 = em.createQuery(
					"select new jpql.MemberDTO(m.username, m.age) from Member m")
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
