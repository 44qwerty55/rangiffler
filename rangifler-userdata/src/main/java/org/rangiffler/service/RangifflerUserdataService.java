package org.rangiffler.service;

import jakarta.annotation.Nonnull;
import org.rangiffler.data.FriendEntity;
import org.rangiffler.data.UserEntity;
import org.rangiffler.data.repository.FriendsRepository;
import org.rangiffler.data.repository.UserDataRepository;
import org.rangiffler.model.FriendJson;
import org.rangiffler.model.FriendStatus;
import org.rangiffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RangifflerUserdataService {

    private final UserDataRepository userDataRepository;
    private final FriendsRepository friendsRepository;

    @Autowired
    public RangifflerUserdataService(UserDataRepository userDataRepository, FriendsRepository friendsRepository) {
        this.userDataRepository = userDataRepository;
        this.friendsRepository = friendsRepository;
    }

    public @Nonnull
    UserJson updateCurrentUser(@Nonnull UserJson user) {
        UserEntity userEntity = userDataRepository.findByUsername(user.getUsername());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setAvatar(user.getAvatar() != null ? user.getAvatar().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userDataRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }


    public @Nonnull
    List<UserJson> getAllUsers(@Nonnull String username) {
        UUID myUuid = userDataRepository.findByUsername(username).getId();
        List<UserJson> listUsersWithStatus = getAllFriendWithStatus(myUuid);

        List<String> userNames = listUsersWithStatus.stream().map(UserJson::getUsername).collect(Collectors.toList());
        userNames.add(username);
        List<UserEntity> listUsersWithoutStatus = userDataRepository.findByUsernameNotIn(userNames);
        List<UserJson> listAllUsers = listUsersWithoutStatus
                .stream()
                .map(UserJson::fromEntity)
                .collect(Collectors.toList());
        listAllUsers.forEach(us -> us.setFriendStatus(FriendStatus.NOT_FRIEND));
        listAllUsers.addAll(listUsersWithStatus);
        return listAllUsers;
    }

    public List<UserJson> getFriends(@Nonnull String username) {
        UUID myUuid = userDataRepository.findByUsername(username).getId();
        List<UserJson> listUsersWithStatus = getAllFriendWithStatus(myUuid);
        listUsersWithStatus.removeIf(us -> us.getFriendStatus() != FriendStatus.FRIEND);
        return listUsersWithStatus;
    }

    public List<UserJson> getInvitations(@Nonnull String username) {
        UUID myUuid = userDataRepository.findByUsername(username).getId();
        List<UserJson> listUsersWithStatus = getAllFriendWithStatus(myUuid);
        listUsersWithStatus.removeIf(us -> us.getFriendStatus() != FriendStatus.INVITATION_RECEIVED);
        return listUsersWithStatus;
    }


    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userEntity = userDataRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setUsername(username);
            return UserJson.fromEntity(userDataRepository.save(userEntity));
        }
        return UserJson.fromEntity(userEntity);
    }

    public UserJson acceptOrRemoveInvitation(FriendJson friendJson, FriendStatus status) {
        UUID myUuid = userDataRepository.findByUsername(friendJson.getMyUsername()).getId();
        FriendEntity friendEntity = new FriendEntity();
        friendEntity.setUser_id(myUuid);
        friendEntity.setFriend_id(friendJson.getId());
        Example<FriendEntity> example = Example.of(friendEntity);
        Optional<FriendEntity> optionalFriendEntity = friendsRepository.findOne(example);
        FriendEntity result;
        if (optionalFriendEntity.isPresent()) {
            result = optionalFriendEntity.get();
            result.setFriendStatus(status);
            result = friendsRepository.save(result);
            saveFriendStatus(friendJson, status, myUuid);
        } else {
            friendEntity.setFriendStatus(status);
            result = friendsRepository.save(friendEntity);
            saveFriendStatus(friendJson, status, myUuid);
        }
        Optional<UserEntity> friendUserById = userDataRepository.findById(result.getFriend_id());
        UserJson userFriendJson;
        if (friendUserById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find friend by given id: " + friendEntity.getFriend_id());
        } else {
            UserEntity friendUserEntity = friendUserById.get();
            userFriendJson = UserJson.fromEntity(friendUserEntity);
            userFriendJson.setFriendStatus(status);
        }
        return userFriendJson;
    }

    public UserJson sendInvitation(FriendJson friendJson) {

        UUID myUuid = userDataRepository.findByUsername(friendJson.getMyUsername()).getId();
        FriendEntity friendEntity = new FriendEntity();
        friendEntity.setUser_id(myUuid);
        friendEntity.setFriend_id(friendJson.getId());
        Example<FriendEntity> example = Example.of(friendEntity);
        Optional<FriendEntity> optionalFriendEntity = friendsRepository.findOne(example);
        FriendEntity result;

        if (optionalFriendEntity.isPresent()) {
            result = optionalFriendEntity.get();
            if (result.getFriendStatus() == FriendStatus.NOT_FRIEND) {
                result.setFriendStatus(FriendStatus.INVITATION_SENT);
                result = friendsRepository.save(result);

                saveFriendStatus(friendJson, FriendStatus.INVITATION_RECEIVED, myUuid);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find friend by given id: " + friendEntity.getFriend_id());
            }
        } else {
            friendEntity.setFriendStatus(FriendStatus.INVITATION_SENT);
            result = friendsRepository.save(friendEntity);

            saveFriendStatus(friendJson, FriendStatus.INVITATION_RECEIVED, myUuid);
        }

        Optional<UserEntity> friendUserById = userDataRepository.findById(result.getFriend_id());
        UserJson userFriendJson;
        if (friendUserById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find friend by given id: " + friendEntity.getFriend_id());
        } else {
            UserEntity friendUserEntity = friendUserById.get();
            userFriendJson = UserJson.fromEntity(friendUserEntity);
            userFriendJson.setFriendStatus(FriendStatus.INVITATION_SENT);
        }
        return userFriendJson;
    }

    private void saveFriendStatus(FriendJson friendJson, FriendStatus friendStatus, UUID myUuid) {
        FriendEntity friendWithMeEntity = new FriendEntity();
        friendWithMeEntity.setUser_id(friendJson.getId());
        friendWithMeEntity.setFriend_id(myUuid);
        Example<FriendEntity> example = Example.of(friendWithMeEntity);
        Optional<FriendEntity> optionalFriendEntity = friendsRepository.findOne(example);
        FriendEntity result;
        if (optionalFriendEntity.isPresent()) {
            result = optionalFriendEntity.get();
            result.setFriendStatus(friendStatus);
            friendsRepository.save(result);
        } else {
            friendWithMeEntity.setFriendStatus(friendStatus);
            friendsRepository.save(friendWithMeEntity);
        }
    }

    private UserJson findById(UUID idUser, FriendStatus status) {
        Optional<UserEntity> userById = userDataRepository.findById(idUser);
        UserJson userJson;
        if (userById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find friend by given id: " + idUser);
        } else {
            UserEntity userEntity = userById.get();
            userJson = UserJson.fromEntity(userEntity);
            userJson.setFriendStatus(status);
        }
        return userJson;
    }

    private List<UserJson> getAllFriendWithStatus(@Nonnull UUID myUuid) {
        FriendEntity friendEntity = new FriendEntity();
        friendEntity.setUser_id(myUuid);
        Example<FriendEntity> example = Example.of(friendEntity);
        List<FriendEntity> optionalFriendEntity = friendsRepository.findAll(example);
        List<UserJson> listUsers = new ArrayList<>();
        optionalFriendEntity.forEach(fr -> {
            UserJson userJson = findById(fr.getFriend_id(), fr.getFriendStatus());
            listUsers.add(userJson);
        });
        return listUsers;
    }

}
    

