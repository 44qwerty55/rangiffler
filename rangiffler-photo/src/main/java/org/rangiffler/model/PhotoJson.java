package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rangiffler.data.PhotoEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PhotoJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("country")
    private CountryJson countryJson;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    public PhotoJson() {
    }

    public static PhotoJson fromEntity(PhotoEntity entity) {
        CountryJson countryJson = new CountryJson();
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

    public UUID getId() {
        return id;
    }

    public CountryJson getCountryJson() {
        return countryJson;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCountryJson(CountryJson countryJson) {
        this.countryJson = countryJson;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
