package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.model.FriendJson;
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

@Component
public class UserClient {

    private final WebClient webClient;
    private final String rangifflerUserBaseUri;

    @Autowired
    public UserClient(WebClient webClient,
                      @Value("${rangiffler-userdata.base-uri}") String rangifflerUserBaseUri) {
        this.webClient = webClient;
        this.rangifflerUserBaseUri = rangifflerUserBaseUri;
    }

    public @Nonnull
    UserJson updateCurrentUser(@Nonnull UserJson user) {
        return webClient.post()
                .uri(rangifflerUserBaseUri + "/updateUser")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull
    List<UserJson> getAllUsers(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserBaseUri + "/users").queryParams(params).build().toUri();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public @Nonnull
    UserJson getCurrentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserBaseUri + "/currentUser").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public List<UserJson> getFriends(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserBaseUri + "/friends").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public List<UserJson> getInvitations(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(rangifflerUserBaseUri + "/invitations").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    public UserJson removeUserFromFriends(@Nonnull FriendJson friendJson) {
        return webClient.post()
                .uri(rangifflerUserBaseUri + "/friends/remove")
                .body(Mono.just(friendJson), FriendJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public UserJson acceptInvitation(@Nonnull FriendJson friendJson) {
        return webClient.post()
                .uri(rangifflerUserBaseUri + "/friends/submit")
                .body(Mono.just(friendJson), FriendJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public UserJson sendInvitation(@Nonnull FriendJson friendJson) {
        return webClient.post()
                .uri(rangifflerUserBaseUri + "/users/invite")
                .body(Mono.just(friendJson), FriendJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }
}
