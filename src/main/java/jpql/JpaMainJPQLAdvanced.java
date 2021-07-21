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

        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
