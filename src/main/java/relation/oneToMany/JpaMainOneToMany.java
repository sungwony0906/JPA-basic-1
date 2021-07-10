package relation.oneToMany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

// 일대다 관계는 실무에서 잘 사용하지 않는다
// 수정된 엔티티와 실제 데이터가 변경된 테이블이 다르기 때문에 디버깅하기 어렵기 때문에 좋지 않은 모델이다
// - 엔티티가 관리하는 외래 키가 다른 테이블에 있다
// - 연관관계 관리를 위해 추가로 UPDATE SQL을 실행한다.
public class JpaMainOneToMany {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = new Member();
			member.setUsername("member1");
			em.persist(member);

			Team team = new Team();
			team.setName("teamA");
			team.getMembers().add(member);

			em.persist(team);

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
