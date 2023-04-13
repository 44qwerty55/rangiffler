package org.rangiffler.data;


import jakarta.persistence.*;
import org.rangiffler.model.FriendStatus;

import java.util.UUID;

@Entity
@Table(name = "users_friends", schema = "public", catalog = "rangiffler-userdata")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID user_id;

    @Column(name = "friend_id", nullable = false)
    private UUID friend_id;

    @Column(name = "friend_Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(UUID friend_id) {
        this.friend_id = friend_id;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
