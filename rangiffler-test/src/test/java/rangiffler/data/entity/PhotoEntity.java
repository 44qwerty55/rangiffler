package rangiffler.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import rangiffler.model.PhotoJson;


import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class PhotoEntity {

    private UUID id;
    private String username;
    private byte[] photo;
    private String description;
    private UUID countriesId;
    private String name;
    private String code;

    public static PhotoEntity fromJson(PhotoJson photoJson) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setUsername(photoJson.getUsername());
        photoEntity.setCode(photoJson.getCountryJson().getCode());
        photoEntity.setName(photoJson.getCountryJson().getName());
        photoEntity.setCountriesId(photoJson.getCountryJson().getId());
        photoEntity.setDescription(photoJson.getDescription());
        photoEntity.setPhoto(photoJson.getPhoto().getBytes(StandardCharsets.UTF_8));
        return photoEntity;
    }

}
