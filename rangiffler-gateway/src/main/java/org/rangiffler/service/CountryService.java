package org.rangiffler.service;

import org.rangiffler.model.CountryJson;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CountryService {

    private final CountryJson countrie = new CountryJson(UUID.randomUUID(), "KZ", "Kazakhstan");

    public CountryJson getCountryByCode(String code) {
        return countrie;
    }
}
