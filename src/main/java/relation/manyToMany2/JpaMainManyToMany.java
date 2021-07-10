package relation.manyToMany2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

			MemberProduct memberProduct = new MemberProduct();
			memberProduct.addOrder(member, product);
			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
