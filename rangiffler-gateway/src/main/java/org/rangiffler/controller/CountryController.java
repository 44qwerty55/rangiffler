package org.rangiffler.controller;

import java.util.List;
import org.rangiffler.model.CountryJson;
import org.rangiffler.service.GeoClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

  private final GeoClient geoClient;

  public CountryController(GeoClient geoClient) {
    this.geoClient = geoClient;
  }

  @GetMapping("/countries")
  public List<CountryJson> getAllCountries() {
    return geoClient.getAllCountries();
  }

  @GetMapping("/country")
  public CountryJson getCountry(String code) {
    return geoClient.getCountryByCode(code);
  }

}
