package org.rangiffler.data;

import jakarta.persistence.*;
import org.rangiffler.model.PhotoJson;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Entity
@Table(name = "photos")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(name = "photo", columnDefinition = "bytea", nullable = true)
    private byte[] photo;

    @Column(nullable = true)
    private String description;

    @Column(name = "countries_id")
    private UUID countriesId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
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


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getCountriesId() {
        return countriesId;
    }

    public void setCountriesId(UUID countriesId) {
        this.countriesId = countriesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
