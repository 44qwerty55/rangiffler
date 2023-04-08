package org.rangiffler.controller;

import org.rangiffler.model.UserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.rangiffler.service.RangifflerUserdataService;

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
    public List<UserJson> users() {
        return rangifflerUserdataService.getAllUsers();
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@RequestParam String username) {
        return rangifflerUserdataService.getCurrentUserOrCreateIfAbsent(username);
    }

    @PostMapping("/updateUser")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return rangifflerUserdataService.updateCurrentUser(user);
    }


}
