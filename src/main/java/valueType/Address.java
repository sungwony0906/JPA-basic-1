package valueType;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor //embeddable 객체는 public/protected 기본 생성자가 반드시 필요한 것으로 보임
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipCode;

    //동등성 비교를 위한 equals / hashcode override
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                       Objects.equals(street, address.street) &&
                       Objects.equals(zipCode, address.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipCode);
    }
}