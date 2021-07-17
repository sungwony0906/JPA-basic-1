package proxy;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainCascade {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Child child1 = new Child();
			Child child2 = new Child();

			Parent parent = new Parent();
			parent.addChild(child1);
			parent.addChild(child2);

			//일반적으로 persist를 개별로 해주어야 한다
			//em.persist(parent);
			//em.persist(child1);
			//em.persist(child2);

			//cascade All로 처리해주는 경우 한번에 영속성 추가해줄 수 있다
			em.persist(parent);

			// CASCADE - 주의!
			// 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
			// 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐이다

			// CASCADE 종류
			// - ALL : 모두 적용
			// - PERSIST : 영속
			// 이 밑으로는 잘 사용하지 않음
			// - REMOVE : 삭제
			// - MERGE : 병합
			// - REFRESH : REFRESH
			// - DETACH : DETACH

			em.flush();
			em.clear();

			// 고아 객체 - orphanRemoval
			// 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 삭제
			// 참조가 제거된 엔티티는 다른 고셍서 참조하지 않는 고아 객체로 보고 삭제하는 기능
			// 참조하는 곳이 하나일 때 / 특정 엔티티가 개인 소유하는 경우에만 사용해야 함
			// ex - 게시물의 첨부파일 / 댓글
			// DDD의 Aggregate Root 개념을 구현할 때 유용하다
			Parent findParent = em.find(Parent.class, parent.getId());
			findParent.getChildList().remove(0); // delete 쿼리가 날아감

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
