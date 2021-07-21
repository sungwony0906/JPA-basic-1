package jpql;

import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainJPQLAdvanced {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            //경로 표현식
            //상태 필드 : 경로 탐색의 끝, 탐색 X
            String query = "select m.username from Member m";
            em.createQuery(query, String.class)
                    .getResultList();

            //단일 값 연관 경로 : 묵시적 내부 조인(inner join) 발생, 탐색 O
            // => 실무에서 사용하지 말 것! (묵시적 join이 발생하는 경우 추적이 어렵다)
            String query2 = "select m.team from Member m";
            String query2_탐색추가 = "select m.team.name from Member m";
            em.createQuery(query2, Member.class)
                    .getResultList();
            em.createQuery(query2_탐색추가, String.class)
                    .getResultList();

            //컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 X
            // => 실무에서 사용하지 말 것
            String query3 = "select t.memberList from Team t";
            String query3_탐색추가불가 = "select t.memberList.size from Team t";
            String query3_명시적조인 = "select m.username from Team t join t.memberList m";
            em.createQuery(query3, Collections.class)
                    .getResultList();
            em.createQuery(query3_탐색추가불가, Integer.class)
                    .getSingleResult();
            em.createQuery(query3_명시적조인, String.class)
                    .getResultList();

            Team teamA = new Team();
            teamA.setName("TEAM_A");
            Team teamB = new Team();
            teamB.setName("TEAM_B");
            em.persist(teamA);
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            //페치 조인
            //실무에서 정말정말 중요함
            //SQL 조인 종류 X, JPQL에서 성능 최적화를 위해 제공하는 기능
            //연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
            String query4 = "select m from Member m join fetch m.team";
            // SQL --> SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID = T.ID
            em.createQuery(query4, Member.class)
                    .getResultList();

            String query4_outer = "select m from Member m left join fetch m.team";
            em.createQuery(query4_outer, Member.class)
                    .getResultList();

            //컬렉션 페치 조인
            String query5 = "select t from Team t join fetch t.memberList";
            // SQL --> SELECT T.*, M.* FROM TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM_ID
            // 데이터가 늘어날 수 있다
            em.createQuery(query5, Team.class)
                    .getResultList()
                    .size(); // --> SIZE = 3

            //SQL의 DISTINCT는 중복된 결과를 제거하는 명령
            //JPQL의 DISTINCT는 2가지 기능을 제공
            // 1. SQL에 DISTINCT 키워드 추가 ==> 데이터가 완전히 동일해야 중복을 제거
            // 2. 애플리케이션에서 엔티티 중복 제거 ==> 같은 식별자를 가진 TEAM 엔티티를 제거
            String query5_distinct = "select distinct t from Team t join fetch t.memberList";
            em.createQuery(query5_distinct, Team.class)
                    .getResultList()
                    .size(); // --> SIZE = 2

            //페치 조인과 일반 조인의 차이
            //일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
            String query5_일반조인 = "select t From Team t join t.members m";
            // -> SELECT team.id, team.name FROM Team team inner join Member member on team.id = member.team_id
            String query5_페치조인 = "select t From Team t join fetch t.members m";
            // -> SELECT team.id, team.name, member.id, member.team_id, member.user_name, member.age FROM Team team inner join Member member on team.id = member.team_id
            
            //페치 조인의 특징과 한계
            //페치 조인 대상에는 별칭을 줄 수 없다 - 하이버네이트는 가능하지만 사용하지 않는 것이 관례이다
            String query6_페치조인대상별칭_비권장 = "select t from Team t join fetch t.members m where m.age > 10"; // 이런식으로 사용하면 안된다
            //예를 들어 팀에 속한 특정 멤버들만 가져오는 경우 이로 인해 문제가 발생할 여지가 크다 나머지가 삭제되버릴 가능성도 있다

            //둘 이상의 컬렉션은 페치 조인 할 수 없다.
            //oneToMany * oneToMany

            //컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResult)를 SQL레벨에서 사용할 수 없다.
            //페치조인 과정에서 데이터가 늘어날 수 있기 때문
            //경고를 남기고 메모리에서 페이징한다(매우 위험!)

            //페이징 우회 -> LAZY LOADING + BATCH SIZE
            String query7_페이징우회 = "select t from Team t";
            em.createQuery(query7_페이징우회, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            //페치 조인 - 정리
            //페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
            //여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적

            //다형성 쿼리
            //Super - Sub
            String query8 = "select i from Item i where type(i) IN (Book, Movie)";
            //SQL -> select i.* from Item i where i.DTYPE in ('B', 'M')
            String query9 = "select i from Item i where treat(i as Book).author = 'kim'";
            //SQL -> select i.* from Item i where i.DTYPE = 'B' and i.author = 'kim'

            //엔티티 직접 사용 - 기본 키 값
            //JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용
            String query10 = "select m from Member m where m = :member";
            String query10_식별자 = "select m from Member m where m.id = :memberId";
            em.createQuery(query10, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            em.createQuery(query10_식별자, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();

            //엔티티 직접 사용 - 외래 키 값
            String query11 = "select m from Member m where m.team = :team";
            String query11_식별자 = "select m from Member m where m.team.id = :teamId";
            em.createQuery(query11, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            em.createQuery(query11_식별자, Member.class)
                    .setParameter("teamId", teamA.getId())
                    .getResultList();

            //Named쿼리
            //createNamedQuery : 미리 정의된 쿼리를 사용한다
            em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            //벌크 연산
            // ex) 재고가 10개 미만인 모든 상품의 가격을 10% 상승
            // JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL이 실행
            //UPDATE, DELETE 지원
            //INSERT SELECT 가능

            Member memberSync = new Member();
            memberSync.setUsername("회원10");
            memberSync.setAge(10);
            memberSync.setTeam(teamA);
            em.persist(memberSync);

            //FLUSH 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                            .executeUpdate();

            System.out.println("resultCount = " + resultCount);
            System.out.println("memberSync age = "+memberSync.getAge()+"살"); // 10살

            //벌크 연산 주의
            //벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리 (싱크 문제 발생)
            //해결책 1. 벌크 연산을 먼저 실행
            //해결책 2. 벌크 연산을 수행 후 영속성 컨텍스트 초기화

            em.clear(); //초기화, 상위 엔티티는 준영속상태로 버려야 한다

            Member findMember = em.find(Member.class, memberSync.getId());
            System.out.println("findMember age = "+findMember.getAge()+"살"); // 20살

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
