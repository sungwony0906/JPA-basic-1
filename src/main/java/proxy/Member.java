package proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    // 연관관계의 주인은 비지니스적으로 중요도와 상관없이 외래 키가 있는 곳을 주인으로 정해라
    // 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
