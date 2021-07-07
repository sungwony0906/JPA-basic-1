package hellojpa;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity // @Entity가 붙은 클래스는 JPA가 관리하는 엔티티라고 한다
//@Entity(name = "NAME") JPA에서 사용할 엔티티 이름을 지정한다(미지정시 클래스 이름을 그대로 사용 - 추천)
//@Table(name = "MEMBER", catalog = "CATALOG", schema = "SCHEMA", uniqueConstraints = "INDEX") 관례에 따라 entity와 테이블 명이 같은 경우 기술하지 않아도 무방
public class Member {

	//기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)
	//final 클래스, enum, interface, inner 클래스 사용 불가
	//저장할 필드에 final 사용 불가

	public Member() {
	}

	public Member(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	private Long id;
	//DDL 생성 기능 - DDL 생성에만 영향을 주고 JPA 실행에 영향이 가지 않는다
	//@Column(name = "USERNAME", unique = true, length = 10)
	//insertable, updatable - 등록 변경 가능 여부
	//nullable - Not null 제약조건
	//unique - Unique 제약 조건 (이름이 랜덤으로 생성되어 잘 사용하지 않고 @Table uniqueConstraints로 대체)
	//columnDefinition - 데이터베이스 컬럼 정보를 직접 생성
	//length - 문자 길이의 제약조건, String만 사용
	private String name;

	@Enumerated(EnumType.STRING)
	//EnumType.Ordinal - Enum의 순서를 기준으로 DB에 저장 - 권장 X
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	// DATE - 데이터베이스 date 타입과 매핑
	// TIME - 데이터베이스 time 타입과 매핑
	// TIMESTAMP - 데이터베이스 timestamp 타입과 매핑
	// java.util.Date, java.util.Calendar 매핑시 사용
	// LocalDate, LocalDateTime을 사용할 대 생략 가능(최신 하이버네이트 지원)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	@Lob
	// 매핑하는 필드 타입에 따라 BLOB/CLOB 매핑
	// CLOB : String, char[], java.sql.CLOB
	// BLOB : byte[], java.sql.BLOB
	private String description;

	@Transient
	// 컬럼 매핑을 원하지 않을 때 사용
	private int temp;
}
