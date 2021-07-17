package valueType;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

//값 타입의 클래스를 선언할 때는 Embeddable 어노테이션을 붙여준다
@Embeddable
@Getter @Setter
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
