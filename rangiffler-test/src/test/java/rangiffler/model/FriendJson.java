package rangiffler.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class FriendJson {

    private UUID id;
    private String username;
    private FriendStatus friendStatus;
    private String myUsername;

    public FriendJson fromUserJson(UserJson user, UserJson friend, FriendStatus friendStatus) {

        FriendJson friendJson = new FriendJson();
        friendJson.setId(friend.getId())
                .setUsername(friend.getUsername())
                .setMyUsername(user.getUsername())
                .setFriendStatus(friendStatus);
        return friendJson;
    }
}
