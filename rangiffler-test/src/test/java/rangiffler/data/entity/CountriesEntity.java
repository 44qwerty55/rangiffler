package rangiffler.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class CountriesEntity {

    private UUID id;
    private String name;
    private String code;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountriesEntity that = (CountriesEntity) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}
