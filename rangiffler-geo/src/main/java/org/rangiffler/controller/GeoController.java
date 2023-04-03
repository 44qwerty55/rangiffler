package org.rangiffler.controller;

import org.rangiffler.model.CountriesJson;
import org.rangiffler.service.RangifflerGeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GeoController {

    private static final Logger LOG = LoggerFactory.getLogger(GeoController.class);

    private final RangifflerGeoService rangifflerGeoService;

    @Autowired
    public GeoController(RangifflerGeoService rangifflerGeoService) {
        this.rangifflerGeoService = rangifflerGeoService;
    }

    @GetMapping("/countries")
    public List<CountriesJson> countries() {
        return rangifflerGeoService.getAllCategories();
    }

    @GetMapping("/country")
    public CountriesJson country(@RequestParam String code) {
        return rangifflerGeoService.getCountryByCode(code);
    }
}
