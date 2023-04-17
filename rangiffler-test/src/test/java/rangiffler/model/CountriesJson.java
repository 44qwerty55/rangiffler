package rangiffler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import rangiffler.data.entity.CountriesEntity;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class CountriesJson {

    private UUID id;
    private String name;
    private String code;

    public static CountriesJson fromEntity(CountriesEntity countriesEntity) {
        CountriesJson countriesJson = new CountriesJson();
        countriesJson.setId(countriesEntity.getId())
                .setCode(countriesEntity.getCode())
                .setName(countriesEntity.getName());
        return countriesJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountriesJson that = (CountriesJson) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}
