package relation.advanced;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

// 공통으로 사용하는 속성을 상속받아서 사용하여 반복적인 코드를 줄이고 싶을때 사용한다
// 직접 생성하여 사용할 일이 없으므로 추상 클래스로 생성하는 것을 권장
@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdDatetime;
    private String modifiedBy;
    private LocalDateTime lastModifiedDateTime;
}
