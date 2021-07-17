package valueType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Member{

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    //private LocalDateTime startDate;
    //private LocalDateTime endDate;

    //값 타입의 클래스로 대체
    @Embedded // 값 타입의 클래스를 사용할 때는 Embedded 어노테이션을 붙여준다
    private Period period;

    //private String city;
    //private String street;
    //private String zipCode;

    @Embedded
    private Address address;

    //만약 동일한 값 타입 클래스 두개를 쓰고싶다면
    @Embedded
    //속성을 override하여 사용할 수 있다
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    //값 타입 컬렉션
    //값 타입을 하나 이상 저장할 떄 사용
    //@ElementCollection @CollectionTable 사용
    //데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없어 별도의 테이블을 생성해 매핑한다

    //참고 : 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME") // 단일 값의 경우 예외적으로 column으로 지정한 이름을 테이블 명으로 생성해 준다
    //create table FOOD_NAME
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns =
        @JoinColumn(name = "MEMBER_ID")
    )
    //create table ADDRESS
    private List<Address> addressHistory = new ArrayList<>();

    //실무에서는 이렇게 사용하는걸 고려하라
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressEntityHistory = new ArrayList<>();
}
