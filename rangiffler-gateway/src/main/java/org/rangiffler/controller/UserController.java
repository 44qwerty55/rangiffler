package org.rangiffler.controller;

import java.util.List;
import org.rangiffler.model.UserJson;
import org.rangiffler.service.UserClient;
import org.rangiffler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;
  private final UserClient userClient;

  @Autowired
  public UserController(UserService userService, UserClient userClient) {
    this.userService = userService;
    this.userClient = userClient;
  }

  @GetMapping("/users")
  public List<UserJson> getAllUsers() {
    return userClient.getAllUsers();
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
  public List<UserJson> getFriendsByUserId() {
    return userService.getFriends();
  }

  @GetMapping("invitations")
  public List<UserJson> getInvitations() {
    return userService.getInvitations();
  }

  @PostMapping("users/invite/")
  public UserJson sendInvitation(@RequestBody UserJson user) {
    return userService.sendInvitation(user);
  }

  @PostMapping("friends/remove")
  public UserJson removeFriendFromUser(@RequestBody UserJson friend) {
    return userService.removeUserFromFriends(friend);
  }

  @PostMapping("friends/submit")
  public UserJson submitFriend(@RequestBody UserJson friend) {
    return userService.acceptInvitation(friend);
  }

  @PostMapping("friends/decline")
  public UserJson declineFriend(@RequestBody UserJson friend) {
    return userService.declineInvitation(friend);
  }

}
