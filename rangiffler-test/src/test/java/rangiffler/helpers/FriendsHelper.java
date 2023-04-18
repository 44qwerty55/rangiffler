package rangiffler.helpers;

import org.apache.http.HttpStatus;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.jupiter.annotation.User;
import rangiffler.model.FriendJson;
import rangiffler.model.UserJson;

public class FriendsHelper {

    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();

    public UserJson inviteFriends(FriendJson friendJson) {
        return rangifflerUserDataClient
                 .postFriendInvitation(friendJson)
                 .assertThat()
                 .statusCode(HttpStatus.SC_OK)
                .extract().as(UserJson.class);
    }

    public UserJson submitFriends(FriendJson friendJson) {
        return rangifflerUserDataClient
                .postFriendSubmit(friendJson)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UserJson.class);
    }
}
