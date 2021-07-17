package proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    // 프록시와 즉시로딩 주의
    // - 가급적 지연 로딩만 사용 (특히 실무에서)
    // - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
    // - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
    // - @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
    // - @OneToMany, @ManyToMany는 기본이 지연 로딩

    //@ManyToOne(fetch = FetchType.LAZY) //지연 로딩 LAZY를 사용 - team을 proxy 객체로 조회한다
    @ManyToOne(fetch = FetchType.EAGER) //즉시 로딩 EAGER를 사용 - 처음부터 연관관계의 엔티티를 모두 조회해온다
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
