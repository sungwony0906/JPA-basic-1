package relation.manyToMany2;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ORDER")
@Getter @Setter
public class MemberProduct {

    //연결 테이블에서도 PK를 별도로 가져가는 것이 실무적으로 유리함
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    //필요한 추가 데이터를 추가할 수 있다
    private int count;
    private int price;
    private LocalDateTime orderDateTime;

    public void addOrder(Member member, Product product){
        this.member = member;
        this.product = product;
        member.getMemberProducts().add(this);
        product.getMemberProducts().add(this);
    }
}
