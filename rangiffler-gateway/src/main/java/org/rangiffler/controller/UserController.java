package org.rangiffler.controller;

import org.rangiffler.model.FriendJson;
import org.rangiffler.model.UserJson;
import org.rangiffler.service.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/users")
    public List<UserJson> getAllUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userClient.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserJson getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userClient.getCurrentUser(username);
    }

    @PatchMapping("/currentUser")
    public UserJson updateCurrentUser(@AuthenticationPrincipal Jwt principal,
                                      @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUsername(username);
        return userClient.updateCurrentUser(user);
    }

    @GetMapping("/friends")
    public List<UserJson> getFriendsByUserId(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userClient.getFriends(username);
    }

    @PostMapping("friends/submit")
    public UserJson submitFriend(@AuthenticationPrincipal Jwt principal,
                                 @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return userClient.acceptInvitation(FriendJson.fromUserJson(friend)
                .setMyUsername(username));
    }

    @PostMapping("users/invite/")
    public UserJson sendInvitation(@AuthenticationPrincipal Jwt principal,
                                   @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        return userClient.sendInvitation(FriendJson.fromUserJson(user)
                .setMyUsername(username));
    }

    @GetMapping("invitations")
    public List<UserJson> getInvitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userClient.getInvitations(username);
    }


    @PostMapping("friends/remove")
    public UserJson removeFriendFromUser(@AuthenticationPrincipal Jwt principal,
                                         @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return userClient.removeUserFromFriends(FriendJson.fromUserJson(friend)
                .setMyUsername(username));
    }

    @PostMapping("friends/decline")
    public UserJson declineFriend(@AuthenticationPrincipal Jwt principal,
                                  @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return userClient.removeUserFromFriends(FriendJson.fromUserJson(friend)
                .setMyUsername(username));
    }

}
