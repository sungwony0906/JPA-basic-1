package relation.manyToMany1;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

// 다대다 관계는 실무에서 사용해선 안된다
// 중계 테이블을 생성해 커버하는 것이 옳바른 접근이다
// 연결 테이블이 단순히 연결만 하고 끝나지 않는 경우가 많은데 해당 경우를 커버할 수 없음
// 예) MEMBER와 PRODUCT를 연결하는 MEMBER_PRODUCT 테이블에 주문시간, 수량 같은 데이터가 들어올 수 있다
// 중간 테이블이 숨겨져 있기 때문에 쿼리를 추적하기도 어렵다

// 다대다 한계 극복 -> MEMBER_PRODUCT를 엔티티로 승격시킨다
public class JpaMainManyToMany {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = new Member();
			member.setUsername("member1");
			em.persist(member);

			Product product = new Product();
			product.setName("productA");
			em.persist(product);

			member.getProducts().add(product);
			product.getMembers().add(member);

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
