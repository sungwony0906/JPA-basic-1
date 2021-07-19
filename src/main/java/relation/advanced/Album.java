package relation.advanced;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorColumn(name = "AAA") // DiscriminatorColumn에 들어갈 값을 지정
public class Album extends Item{

    private String artist;
}
