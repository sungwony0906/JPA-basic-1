package jpql;

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
import org.hibernate.annotations.BatchSize;

@Entity
@Getter @Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    //@BatchSize(size = 100) // LAZY LOADING시 식별자를 모아서 IN QUERY로 조회할 수 있도록 해준다
    @OneToMany(mappedBy = "team")
    @Setter(AccessLevel.NONE)
    private List<Member> memberList = new ArrayList<>();

    public void addMember(Member member){
        member.setTeam(this);
        memberList.add(member);
    }
}
