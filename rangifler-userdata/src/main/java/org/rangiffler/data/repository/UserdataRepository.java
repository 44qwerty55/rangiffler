package org.rangiffler.data.repository;

import org.rangiffler.data.UserEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserdataRepository extends JpaRepository<UserEntity, UUID> {


    @Nullable
    UserEntity findByUsername(String username);

}
