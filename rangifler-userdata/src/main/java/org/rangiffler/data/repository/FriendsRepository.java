package org.rangiffler.data.repository;

import org.rangiffler.data.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {

}
