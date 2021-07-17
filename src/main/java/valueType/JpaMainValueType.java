package valueType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

			Address address = new Address("seoul", "street1", "100");
			//address.setCity("seoul");
			//address.setStreet("street1");
			//address.setZipCode("100");
			member.setAddress(address);

			Member member2 = new Member();
			member2.setUsername("user1");
			member2.setAddress(address); // 값 타입의 공유 참조가 발생

			em.persist(member);
			em.persist(member2);

			//member.getAddress().setCity("newCity"); // 이렇게 수정을 한다면? => member2의 address도 함께 변경

			//객체 타입의 한계
			//객체의 공유 참조는 피할 수 없다..
			//값 타입은 불변 객체로 만들어 부작용을 원천 차단한다

			//불변 객체를 사용할 때 변경하고 싶다면?
			//새로 생성하여 할당한다
			member.setAddress(new Address("seoul", "street2", "200"));

			Address address1 = new Address("city", "street", "100");
			Address address2 = new Address("city", "street", "100");

			// 값 타입은 객체이기 때문에 equals와 hashcode 를 오버라이드하여 동등성 비교를 해야한다
			System.out.println("address1 == address2 : "+(address1 == address2));
			System.out.println("address1 equals address2 : "+(address1.equals(address2)));

			// 별도의 테이블에 저장(FOOD_NAME)
			member.getFavoriteFoods().add("피자");
			member.getFavoriteFoods().add("치킨");

			// 별도의 테이블에 저장(ADDRESS)
			member.getAddressHistory().add(address1);
			member.getAddressHistory().add(address2);

			em.flush();
			em.clear();

			Member findMember = em.find(Member.class, member.getId());
			//값 타입 컬렉션도 지연 로딩 전략 사용
			List<Address> addressHistory = findMember.getAddressHistory();
			for(Address address3 : addressHistory){
				System.out.println("address = " + address3.getCity()); // 조회 쿼리 수행
			}
			Set<String> favoriteFoods = findMember.getFavoriteFoods();
			for(String favoriteFood : favoriteFoods){
				System.out.println("favoriteFood = " + favoriteFood); // 조회 쿼리 수행
			}

			// 값 타입 컬렉션 수정
			// 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
			// 실무에서는 값 타입 컬렉션을 심플하게 쓸수 없다면 일대다 관계를 고려해서 사용하는게 낫다 (Address -> AddressEntity)
			findMember.getFavoriteFoods().remove("치킨"); // deleteAll
			findMember.getFavoriteFoods().add("학식"); // insertAll

			findMember.getAddressHistory().remove(address1); // deleteAll - memberId 가 같은 address를 전부 삭제
			findMember.getAddressHistory().add(new Address("newCity1", "street", "100")); // insertAll

			findMember.getAddressEntityHistory().add(new AddressEntity("city1", "street1", "100"));
			findMember.getAddressEntityHistory().add(new AddressEntity("city2", "street2", "200"));

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
