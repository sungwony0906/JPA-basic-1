package relation.advanced;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;


@Entity
// 기본 전략은 단일 테이블 - 한 테이블에 모든 타입 컬럼 추가
// 단일 테이블 전략에서는 DiscriminatorColumn을 선언하지 않아도 자동 생성된다
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

// JOIN 전략
// 슈퍼타입 서브타입 테이블이 각각 생성된다
// insert 2회 / 조회시 join
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn //구분을 위한 컬럼을 생성해준다. DB관점에서 데이터가 어떤 서브데이터로 인해 생성된 것인지 확인하기에 편리하다

// TABLE_PER_CLASS 전략
// 구현 클래스마다 테이블을 생성
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// item 테이블이 생성되지 않는다
// DiscriminatorColumn이 생성되지 않는다(선언해도 무시)
// 실무해서 사용하지 말아라.. 확장성이 좋지 않음
@Getter @Setter
public abstract class Item extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
