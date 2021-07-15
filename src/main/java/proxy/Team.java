package proxy;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // 연관관계의 거울
    // 사실은 단방향 연관관계가 2개 있는 것이다.
    @OneToMany(mappedBy = "team")
    @Setter(AccessLevel.NONE)
    private List<Member> memberList = new ArrayList<>(); // 관례적으로 초기화를 시켜준다.

    // 양방향 연관관계에서 주의점
    // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자 => 연관관계 편의 메소드를 생성하자
    // 단, 편의 메소드를 양쪽에 생성하지는 않는다
    public void addMember(Member member){
        member.setTeam(this);
        memberList.add(member);
    }
}
