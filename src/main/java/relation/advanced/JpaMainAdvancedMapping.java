package relation.advanced;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import relation.manyToMany1.Member;
import relation.manyToMany1.Product;

public class JpaMainAdvancedMapping {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Movie movie = new Movie();
			movie.setDirector("AAAA");
			movie.setActor("BBBB");
			movie.setName("바람과함께사라지다");
			movie.setPrice(10000);

			movie.setCreatedBy("sungwony");
			movie.setCreatedDatetime(LocalDateTime.now());

			em.persist(movie);

			em.flush();
			em.clear();

			Movie findMovie = em.find(Movie.class, movie.getId());

			// TABLE_PER_CLASS 전략인 경우에 모든 테이블을 UNION 해서 조회하는 단점이 있다
			Item item = em.find(Item.class, movie.getId());

			tx.commit();
		} catch (Exception e){
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
