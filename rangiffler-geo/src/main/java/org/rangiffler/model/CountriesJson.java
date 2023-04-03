package org.rangiffler.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.rangiffler.data.CountriesEntity;

import java.util.Objects;
import java.util.UUID;

public class CountriesJson {

    private UUID id;
    private String name;
    private String code;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CountriesJson fromEntity(CountriesEntity entity) {
        CountriesJson countriesJson = new CountriesJson();
        countriesJson.setId(entity.getId());
        countriesJson.setName(entity.getName());
        countriesJson.setCode(entity.getCode());
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
