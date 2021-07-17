package valueType;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
}
