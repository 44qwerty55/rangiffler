package rangiffler.test.rest;

import io.qameta.allure.AllureId;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.data.dao.UserDataDao;
import rangiffler.generators.UserDataGenerator;
import rangiffler.helpers.FriendsHelper;
import rangiffler.jupiter.annotation.DAO;
import rangiffler.jupiter.annotation.UserData;
import rangiffler.jupiter.extension.DAOResolverUserData;
import rangiffler.jupiter.extension.UsersDataExtension;
import rangiffler.model.FriendJson;
import rangiffler.model.FriendStatus;
import rangiffler.model.UserJson;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith({DAOResolverUserData.class, UsersDataExtension.class})
public class UserDataTest extends BaseTest {

    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();
    @DAO
    private UserDataDao userDataDao;


    @Test
    @AllureId("200")
    @DisplayName("/currentUser Проверка добавления пользователя")
    void addUser() {
        UserJson expected = new UserDataGenerator().createDefaultUserData();
        ValidatableResponse actual = rangifflerUserDataClient
                .getCurrentUserOrCreate(expected.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        expected = UserJson.fromEntity(userDataDao.getUser(expected.getUsername()));
        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @AllureId("201")
    @DisplayName("/updateUser Проверка обновления данных пользователя")
    void updateUser(@UserData(username = "qwerty44433") UserJson expected) {
        expected.setFirstName("New First Name");
        ValidatableResponse actual = rangifflerUserDataClient
                .postUpdateUser(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @AllureId("202")
    @DisplayName("/users/invite Проверка отправки сообщения на добавления в друзья")
    void inviteUser(@UserData(username = "myUser") UserJson user,
                    @UserData(username = "friend") UserJson friend) {

        FriendJson friendJson = new FriendJson().fromUserJson(user, friend, FriendStatus.INVITATION_SENT);

        ValidatableResponse actual = rangifflerUserDataClient
                .postFriendInvitation(friendJson)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        FriendStatus myStatus = userDataDao.getFriendStatus(user, friend);
        FriendStatus friendStatus = userDataDao.getFriendStatus(friend, user);
        Assertions.assertAll(
                () -> assertEquals(FriendStatus.INVITATION_SENT, myStatus),
                () -> assertEquals(FriendStatus.INVITATION_RECEIVED, friendStatus)
        );
    }

    @Test
    @AllureId("203")
    @DisplayName("/friends/submit Проверка принятия сообщения на добавления в друзья")
    void submitUser(@UserData() UserJson user,
                    @UserData() UserJson friend) {

        new FriendsHelper().inviteFriends(new FriendJson().fromUserJson(user, friend, FriendStatus.INVITATION_SENT));

        rangifflerUserDataClient
                .postFriendSubmit(new FriendJson().fromUserJson(friend, user, FriendStatus.FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        FriendStatus myStatus = userDataDao.getFriendStatus(user, friend);
        FriendStatus friendStatus = userDataDao.getFriendStatus(friend, user);
        Assertions.assertAll(
                () -> assertEquals(FriendStatus.FRIEND, myStatus),
                () -> assertEquals(FriendStatus.FRIEND, friendStatus)
        );
    }

    @Test
    @AllureId("204")
    @DisplayName("/friends/remove Проверка отказа на добавления в друзья")
    void removeUserAfterInvite(@UserData() UserJson user,
                               @UserData() UserJson friend) {

        new FriendsHelper().inviteFriends(new FriendJson().fromUserJson(user, friend, FriendStatus.INVITATION_SENT));

        rangifflerUserDataClient
                .postFriendRemove(new FriendJson().fromUserJson(friend, user, FriendStatus.FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        FriendStatus myStatus = userDataDao.getFriendStatus(user, friend);
        FriendStatus friendStatus = userDataDao.getFriendStatus(friend, user);
        Assertions.assertAll(
                () -> assertEquals(FriendStatus.NOT_FRIEND, myStatus),
                () -> assertEquals(FriendStatus.NOT_FRIEND, friendStatus)
        );
    }

    @Test
    @AllureId("205")
    @DisplayName("/friends/remove Удаление из друзей")
    void removeUser(@UserData() UserJson user,
                    @UserData() UserJson friend) {

        new FriendsHelper().inviteFriends(new FriendJson().fromUserJson(user, friend, FriendStatus.INVITATION_SENT));
        new FriendsHelper().submitFriends(new FriendJson().fromUserJson(friend, user, FriendStatus.FRIEND));

        rangifflerUserDataClient
                .postFriendRemove(new FriendJson().fromUserJson(user, friend, FriendStatus.NOT_FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        FriendStatus myStatus = userDataDao.getFriendStatus(user, friend);
        FriendStatus friendStatus = userDataDao.getFriendStatus(friend, user);
        Assertions.assertAll(
                () -> assertEquals(FriendStatus.NOT_FRIEND, myStatus),
                () -> assertEquals(FriendStatus.NOT_FRIEND, friendStatus)
        );
    }
}
