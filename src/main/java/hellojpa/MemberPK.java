package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

@Entity
//테이블 별 시퀀스를 사용하고 싶을 경우 sequenceGenerator 생성
//allocationSize - 데이터베이스 시퀀스의 increment 값 (빈번한 시퀀스 호출을 줄임)
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR", //시퀀스 제네레이터 이름
        sequenceName = "MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 50
)
/* 테이블 전략 */
/*
장점 : 모든 데이터베이스에서 사용할 수 있음
단점 : 성능 이슈
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "MEMBER_SEQ", allocationSize = 1
)
=> create table MY_SEQUENCES(
    sequence_name varchar(255) not null,
    next_val bigint,
    primary key (sequence_name)
)
*/
public class MemberPK {

    //@Id // 직접 할당 : @Id만 사용
    //private String id;

    //@Id
    // 기본 키 생성을 데이터베이스에 위임 - mysql : auto_increment
    // identity 전략은 id를 미리 알 수 없기 때문에 영속성 컨텍스트에 등록하는 시점에 insert 쿼리를 호출한다
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    // 시퀀스를 생성하고 호출하여 아이디를 얻어온다 -> 시퀀스명이 hibernate_sequence로 생성됨
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)

    @Id
    // 사용할 시퀀스 generator를 설정하면 해당 시퀀스를 사용한다
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;
}
