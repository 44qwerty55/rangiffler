package org.rangiffler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.rangiffler.model.FriendStatus;
import org.rangiffler.model.UserJson;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserJson currentUser = new UserJson(UUID.randomUUID(), "Current username", "Current firstName");



  public UserJson getCurrentUser() {
    return currentUser;
  }

  public List<UserJson> getFriends() {
    return List.of(currentUser);
  }

  public UserJson sendInvitation(UserJson user) {
   return currentUser;
  }

  public UserJson acceptInvitation(UserJson friend) {
    return currentUser;
  }

  public UserJson declineInvitation(UserJson friend) {
    return currentUser;
  }


  public UserJson removeUserFromFriends(UserJson friend) {
    return currentUser;
  }

  public List<UserJson> getInvitations() {
    return List.of(currentUser);
  }
}
