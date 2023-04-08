package org.rangiffler.data;


import jakarta.persistence.*;
import org.rangiffler.model.FriendStatus;

import java.util.UUID;

@Entity
@Table(name = "users", schema = "public", catalog = "rangiffler-userdata")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "first_Name", nullable = true)
    private String firstName;

    @Column(name = "last_Name", nullable = true)
    private String lastName;

    @Column(name = "avatar", columnDefinition = "bytea")
    private byte[] avatar;

    @Column(name = "friend_Status", nullable = true)
    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
