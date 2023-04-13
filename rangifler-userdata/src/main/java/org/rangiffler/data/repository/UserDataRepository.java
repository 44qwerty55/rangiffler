package org.rangiffler.data.repository;

import org.rangiffler.data.UserEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserDataRepository extends JpaRepository<UserEntity, UUID> {


    @Nullable
    UserEntity findByUsername(String username);

    List<UserEntity> findByUsernameNot(String username);

    List<UserEntity>  findByUsernameNotIn(List<String> username);

}
