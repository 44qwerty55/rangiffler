package org.rangiffler.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
@Setter
@Getter
public class FriendJson {

    private UUID id;
    private String username;
    private FriendStatus friendStatus;
    private String myUsername;

    public static FriendJson fromUserJson(UserJson userJson) {
        FriendJson friendJson = new FriendJson();
        friendJson.setId(userJson.getId())
                .setUsername(userJson.getUsername());
        return friendJson;

    }

}
