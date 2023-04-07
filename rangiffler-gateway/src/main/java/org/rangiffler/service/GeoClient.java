package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
public class GeoClient {

    private final WebClient webClient;
    private final String rangifflerGeoBaseUri;

    @Autowired
    public GeoClient(WebClient webClient,
                              @Value("${rangiffler-geo.base-uri}") String rangifflerGeoBaseUri) {
        this.webClient = webClient;
        this.rangifflerGeoBaseUri = rangifflerGeoBaseUri;
    }

    public @Nonnull
    List<CountryJson> getAllCountries() {
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerGeoBaseUri + "/countries").build().toUri();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CountryJson>>() {
                })
                .block();
    }

    public @Nonnull
    CountryJson getCountryByCode(@Nonnull String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerGeoBaseUri + "/country").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(CountryJson.class)
                .block();
    }
}
