package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMainEntityMapping {

	// 객체와 테이블 매핑 : @Entity, @Table
	// 필드와 컬럼 매핑 : @Column
	// 기본 키 매핑 : @Id
	// 연관관계 매핑 : @ManyToOne, @JoinColumn

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Member member = new Member(1L, "Hello World");
			member.setId(1L);
			member.setName("HelloWorld");

			em.persist(member);

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
