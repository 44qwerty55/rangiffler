package org.rangiffler.controller;

import org.rangiffler.model.FriendJson;
import org.rangiffler.model.FriendStatus;
import org.rangiffler.model.UserJson;
import org.rangiffler.service.RangifflerUserdataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserdataController {

    private static final Logger LOG = LoggerFactory.getLogger(UserdataController.class);

    private final RangifflerUserdataService rangifflerUserdataService;

    @Autowired
    public UserdataController(RangifflerUserdataService rangifflerUserdataService) {
        this.rangifflerUserdataService = rangifflerUserdataService;
    }

    @GetMapping("/users")
    public List<UserJson> users(@RequestParam String username) {
        return rangifflerUserdataService.getAllUsers(username);
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@RequestParam String username) {
        return rangifflerUserdataService.getFriends(username);
    }

    @GetMapping("/invitations")
    public List<UserJson> invitations(@RequestParam String username) {
        return rangifflerUserdataService.getInvitations(username);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@RequestParam String username) {
        return rangifflerUserdataService.getCurrentUserOrCreateIfAbsent(username);
    }

    @PostMapping("/updateUser")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return rangifflerUserdataService.updateCurrentUser(user);
    }

    @PostMapping("/friends/submit")
    public UserJson acceptInvitation(@RequestBody FriendJson friendJson) {
        return rangifflerUserdataService.acceptOrRemoveInvitation(friendJson, FriendStatus.FRIEND);
    }

    @PostMapping("/users/invite")
    public UserJson sendInvitation(@RequestBody FriendJson friendJson) {
        return rangifflerUserdataService.sendInvitation(friendJson);
    }

    @PostMapping("/friends/remove")
    public UserJson removeUserFromFriends(@RequestBody FriendJson friendJson) {
        return rangifflerUserdataService.acceptOrRemoveInvitation(friendJson, FriendStatus.NOT_FRIEND);
    }


}
