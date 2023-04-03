package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.data.CountriesEntity;
import org.rangiffler.data.repository.CountriesRepository;
import org.rangiffler.model.CountriesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RangifflerGeoService {

    private final CountriesRepository countriesRepository;

    @Autowired
    public RangifflerGeoService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public @Nonnull
    List<CountriesJson> getAllCategories() {
        return countriesRepository.findAll()
                .stream()
                .map(CountriesJson::fromEntity)
                .collect(Collectors.toList());
    }

    public @Nonnull
    CountriesJson getCountryByCode(@Nonnull String code) {
        CountriesEntity countriesEntity = countriesRepository.findByCode(code);
        if (countriesEntity == null) {
            throw new RuntimeException(code);
        }
        return CountriesJson.fromEntity(countriesEntity);
    }
}
    

