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

		emf.close();
	}
}
