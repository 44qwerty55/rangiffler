package rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import rangiffler.data.entity.PhotoEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Getter
@Setter
@Accessors(chain = true)
public class PhotoJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("country")
    private CountriesJson countryJson;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    public PhotoJson() {
    }

    public static PhotoJson fromEntity(PhotoEntity entity) {
        CountriesJson countryJson = new CountriesJson();
        countryJson.setId(entity.getCountriesId());
        countryJson.setName(entity.getName());
        countryJson.setCode(entity.getCode());

        PhotoJson usr = new PhotoJson();
        byte[] photo = entity.getPhoto();
        usr.setId(entity.getId());
        usr.setUsername(entity.getUsername());
        usr.setDescription(entity.getDescription());
        usr.setCountryJson(countryJson);
        usr.setPhoto(photo != null && photo.length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null);
        return usr;
    }

}
