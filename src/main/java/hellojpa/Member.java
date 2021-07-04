package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
