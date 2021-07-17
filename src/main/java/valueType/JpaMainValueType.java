package valueType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainValueType {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Member member = new Member();
			member.setUsername("user1");

			Address address = new Address();
			address.setCity("seoul");
			address.setStreet("street1");
			address.setZipCode("100");
			member.setAddress(address);
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
