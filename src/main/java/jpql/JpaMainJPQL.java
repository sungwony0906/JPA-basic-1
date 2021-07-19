package jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import relation.advanced.Item;

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

			// 페이징
			// firstResult와 maxResult 조건만 추가하면 dialect에 맞게 쿼리 실행
			List<Member> members = em.createQuery("select m from Member m order by m.age desc", Member.class)
											   .setFirstResult(0)
											   .setMaxResults(10)
											   .getResultList();

			System.out.println("result.size = "+members.size());

			// 조인
			// inner join
			String innerJoin = "select m from Member m inner join m.team t";
			List<Member> innerJoinMembers = em.createQuery(innerJoin, Member.class)
													.getResultList();

			// outer join
			String outerJoin = "select m from Member m left join m.team t";
			List<Member> outerJoinMembers = em.createQuery(outerJoin, Member.class)
													.getResultList();

			// seta join
			String setaJoin = "select m from Member m, Team t where m.username = t.name";
			List<Member> setaJoinMembers = em.createQuery(setaJoin, Member.class)
												   .getResultList();

			// 조인 - ON 절
			// 조인 대상 필터링
			String joinOn = "select m from Member m left join m.team t on t.name = 'team'";
			List<Member> joinOnMembers = em.createQuery(joinOn, Member.class)
												 .getResultList();

			// 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1 이상)
			String joinNoRelation = "select m from Member m left join Team t on m.username = t.name";
			List<Member> joinNoRelationMembers = em.createQuery(joinNoRelation, Member.class)
														 .getResultList();

			//서브쿼리
			//JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
			//하이버네이트는 SELECT 절도 가능
			//FROM 절의 서브 쿼리는 현재 JPQL에서 불가능 -> 조인으로 풀 수 있으면 풀어서 해결하고 불가능할 경우 어플리케이션에서 해결
			String subquery = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
			List<Member> subqueryList1 = em.createQuery(subquery, Member.class)
											   .getResultList();

			String subquery2 = "select m from Member m where (select count(o) from Order o where m = o.member) > 0";
			List<Member> subqueryList2 = em.createQuery(subquery2, Member.class)
												 .getResultList();

			// [NOT]EXIST (subquery) : 서브쿼리에 결과가 존재하면 참
			// [ALL] (subquery) : 모두 만족하면 참
			// [ANY, SOME] (subquery) : 조건을 하나라도 만족하면 참
			// [NOT] IN (subquery) : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참

			String existSubquery = "select m from Member m where exists (select t from m.team t where t.name = 'teamA')";
			List<Member> existSubqueryList = em.createQuery(existSubquery, Member.class)
													 .getResultList();

			// 전체 상품 각각의 재고보다 주문량이 많은 주문들
			String allSubquery = "select o from Order o where o.orderAmount > ALL(select p.stockAmount from Product p)";
			List<Member> allSubqueryList = em.createQuery(allSubquery, Member.class)
												   .getResultList();

			// 어떤 팀이든 팀에 소속된 회원
			String anySubquery = "select m from Member m where m.team = ANY(select t from Team t)";
			List<Member> anySubqueryList = em.createQuery(anySubquery, Member.class)
												   .getResultList();

			// JPQL 타입 표현
			String typeQuery = "select m.username, "
									   + "'HELLO', 'SHE ''s', " // 문자
									   + "10L, 10D, 10F, " // 숫자
									   + "true, false " //boolean
									   + "from Member m "
									   + "where m.type = jpql.MemberType.ADMIN";
			em.createQuery(typeQuery);

			//상속 관계에서 특정 타입만 조회하고자 할 때
			String childTypeQuery = "select i from Item i where type(i) = Book";
			em.createQuery(childTypeQuery, Item.class);

			// 조건식
			String caseQuery = "select "
									   + "case when m.age <= 10 then '학생요금' "
									   + "	   when m.age >= 60 then '경로요금' "
									   + "	   else '일반요금' "
									   + "end "
					   		 + "from Member m ";
			em.createQuery(caseQuery, String.class)
					.getResultList();

			//COALESCE : 하나씩 조회해서 null이 아니면 반환
			String coalesceQuery = "select coalesce(m.username, '이름 없는 회원') from Member m ";
			em.createQuery(coalesceQuery, String.class)
					.getResultList();

			//nullif : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
			String nullifQuery = "select nullif(m.username, '관리자') from Member m";
			em.createQuery(nullifQuery, String.class)
					.getResultList();

			// JPQL 기본 함수
			// CONCAT, SUBSTRING, TRIM, LOWER, UPPER, LENTGH, LOCATE, ABS, SQRT, MOD
			// SIZE(컬렉션 크기를 반환), INDEX => JPA 용도
			String concatQuery = "select concat('a', 'b') from Member m";
			String concatQuery2 = "select 'a' || 'b' from Member m"; // concat
			em.createQuery(concatQuery, String.class)
					.getResultList();
			em.createQuery(concatQuery2, String.class)
					.getResultList();

			// 사용자 정의 함수 호출
			// 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다
			// org.hibernate.dialect.XXXDialect -> registerFunction 으로 대부분 미리 등록되어 있음

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
