package rangiffler.helpers;

import org.apache.http.HttpStatus;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.model.FriendJson;
import rangiffler.model.FriendStatus;
import rangiffler.model.UserJson;

public class FriendsHelper {

    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();

    public UserJson inviteFriends(UserJson user, UserJson friend, FriendStatus friendStatus) {
        FriendJson friendJson = new FriendJson().fromUserJson(user, friend, friendStatus);
        return rangifflerUserDataClient
                .postFriendInvitation(friendJson)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UserJson.class);
    }

    public UserJson submitFriends(UserJson user, UserJson friend, FriendStatus friendStatus) {
        FriendJson friendJson = new FriendJson().fromUserJson(user, friend, friendStatus);

        return rangifflerUserDataClient
                .postFriendSubmit(friendJson)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UserJson.class);
    }
}
