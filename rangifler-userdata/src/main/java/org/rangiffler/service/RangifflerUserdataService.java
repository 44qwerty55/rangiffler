package org.rangiffler.service;

import org.rangiffler.data.UserEntity;
import org.rangiffler.data.repository.UserdataRepository;
import jakarta.annotation.Nonnull;
import org.rangiffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RangifflerUserdataService {

    private final UserdataRepository userdataRepository;

    @Autowired
    public RangifflerUserdataService(UserdataRepository userdataRepository) {
        this.userdataRepository = userdataRepository;
    }

    public @Nonnull
    UserJson updateCurrentUser(@Nonnull UserJson user) {
        UserEntity userEntity = userdataRepository.findByUsername(user.getUsername());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setFriendStatus(user.getFriendStatus());
        userEntity.setAvatar(user.getAvatar() != null ? user.getAvatar().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userdataRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }


    public @Nonnull
    List<UserJson> getAllUsers() {
        return userdataRepository.findAll()
                .stream()
                .map(UserJson::fromEntity)
                .collect(Collectors.toList());
    }

    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userEntity = userdataRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setUsername(username);
            return UserJson.fromEntity(userdataRepository.save(userEntity));
        }
        return UserJson.fromEntity(userEntity);
    }
}
    

