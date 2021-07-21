package jpql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import lombok.Getter;
import lombok.Setter;

@Entity
//Named쿼리
//미리 정의해서 이름을 부여해두고 사용하는 JPQL
//정적 쿼리
//어노테이션, XML 정의
//애플리케이션 로딩 시점에 SQL로 파싱해서 들고있는다
//애플리케이션 로딩 시점에 쿼리를 검증
@NamedQuery(
        name = "Member.findByUsername",
        //query = "select m from MemberQQQ m where m.username = :username" //애플리케이션 로딩 시점에 쿼리 검증이 가능
        query = "select m from Member m where m.username = :username"
) // SpringDataJPA에서는 interface Method 위에 @Query로 바로 선언 -> 익명 Named Query
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    private int age;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
