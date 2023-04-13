package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.model.FriendsNameForFoto;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Component
public class PhotoClient {

    private final WebClient webClient;
    private final UserClient userClient;
    private final String rangifflerPhotoBaseUri;

    @Autowired
    public PhotoClient(WebClient webClient,
                       @Value("${rangiffler-photo.base-uri}") String rangifflerPhotoBaseUri,
                       UserClient userClient) {
        this.webClient = webClient;
        this.rangifflerPhotoBaseUri = rangifflerPhotoBaseUri;
        this.userClient = userClient;
    }

    public @Nonnull
    void deletePhoto(@Nonnull UUID photoUUid) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("photoUuid", photoUUid.toString());
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerPhotoBaseUri + "/dellPhoto").queryParams(params).build().toUri();
        webClient.delete()
                .uri(uri)
                .exchangeToMono(response -> Mono.just(response.statusCode()))
                .block();
    }

    public @Nonnull
    PhotoJson addPhoto(@Nonnull PhotoJson photo) {
        return webClient.post()
                .uri(rangifflerPhotoBaseUri + "/addPhoto")
                .body(Mono.just(photo), PhotoJson.class)
                .retrieve()
                .bodyToMono(PhotoJson.class)
                .block();
    }

    public @Nonnull
    PhotoJson editPhoto(@Nonnull PhotoJson photo) {
        return webClient.post()
                .uri(rangifflerPhotoBaseUri + "/editPhoto")
                .body(Mono.just(photo), PhotoJson.class)
                .retrieve()
                .bodyToMono(PhotoJson.class)
                .block();
    }

    public @Nonnull
    List<PhotoJson> getAllPhotosByUsername(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerPhotoBaseUri + "/photos").queryParams(params).build().toUri();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PhotoJson>>() {
                })
                .block();
    }

    public List<PhotoJson> getAllFriendsPhotos(@Nonnull String username) {
        List<UserJson> friends = userClient.getFriends(username);
        FriendsNameForFoto friendsNameForFoto = FriendsNameForFoto.fromUserJson(friends);
        return webClient.post()
                .uri(rangifflerPhotoBaseUri + "/friends/photos")
                .body(Mono.just(friendsNameForFoto), FriendsNameForFoto.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PhotoJson>>() {
                })
                .block();
    }
}
