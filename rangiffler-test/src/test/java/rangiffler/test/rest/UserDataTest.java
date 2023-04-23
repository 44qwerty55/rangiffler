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

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith({DAOResolverUserData.class, UsersDataExtension.class})
public class UserDataTest extends BaseTest {

    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();
    @DAO
    private UserDataDao userDataDao;
    private final FriendsHelper friendsHelper = new FriendsHelper();


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
    void inviteUser(@UserData(username = "myUser202") UserJson user,
                    @UserData(username = "friend202") UserJson friend) {

        FriendJson friendJson = new FriendJson().fromUserJson(user, friend, FriendStatus.INVITATION_SENT);

        ValidatableResponse actual = rangifflerUserDataClient
                .postFriendInvitation(friendJson)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(friend.setFriendStatus(FriendStatus.INVITATION_SENT.name()), actual);

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
    void submitUser(@UserData(username = "myUser203") UserJson user,
                    @UserData(username = "friend203") UserJson friend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);

        ValidatableResponse actual = rangifflerUserDataClient
                .postFriendSubmit(new FriendJson().fromUserJson(friend, user, FriendStatus.FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(user.setFriendStatus(FriendStatus.FRIEND.name()), actual);

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
    void removeUserAfterInvite(@UserData(username = "myUser204") UserJson user,
                               @UserData(username = "friend204") UserJson friend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);

        ValidatableResponse actual = rangifflerUserDataClient
                .postFriendRemove(new FriendJson().fromUserJson(friend, user, FriendStatus.FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(user.setFriendStatus(FriendStatus.NOT_FRIEND.name()), actual);

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
    void removeUser(@UserData(username = "myUser205") UserJson user,
                    @UserData(username = "friend205") UserJson friend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);
        friendsHelper.submitFriends(friend, user, FriendStatus.FRIEND);

        ValidatableResponse actual = rangifflerUserDataClient
                .postFriendRemove(new FriendJson().fromUserJson(user, friend, FriendStatus.NOT_FRIEND))
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(friend.setFriendStatus(FriendStatus.NOT_FRIEND.name()), actual);

        FriendStatus myStatus = userDataDao.getFriendStatus(user, friend);
        FriendStatus friendStatus = userDataDao.getFriendStatus(friend, user);
        Assertions.assertAll(
                () -> assertEquals(FriendStatus.NOT_FRIEND, myStatus),
                () -> assertEquals(FriendStatus.NOT_FRIEND, friendStatus)
        );
    }

    @Test
    @AllureId("206")
    @DisplayName("/users Получение списка всех пользователей [Проверка друга и не друга в списске]")
    void getAllUser(@UserData(username = "myUser206") UserJson user,
                    @UserData(username = "friend206") UserJson friend,
                    @UserData(username = "notFriend206") UserJson notFriend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);
        friendsHelper.submitFriends(friend, user, FriendStatus.FRIEND);

        List<UserJson> response = rangifflerUserDataClient
                .getAllUsers(user.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getList(".", UserJson.class);

        Assertions.assertFalse(response
                .stream()
                .anyMatch(us -> us.getUsername().equals(user.getUsername()))
        );

        List<UserJson> actual = response.stream()
                .filter(us -> us.getUsername().equals(friend.getUsername()) || us.getUsername().equals(notFriend.getUsername()))
                .collect(Collectors.toList());

        List<UserJson> expected = List.of(friend.setFriendStatus(FriendStatus.FRIEND.name()),
                notFriend.setFriendStatus(FriendStatus.NOT_FRIEND.name()));

        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @AllureId("207")
    @DisplayName("/friends Получение списка друзей")
    void getFriendUser(@UserData(username = "myUser2007") UserJson user,
                       @UserData(username = "friend2007") UserJson friend,
                       @UserData(username = "notFriend2007") UserJson notFriend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);
        friendsHelper.submitFriends(friend, user, FriendStatus.FRIEND);

        List<UserJson> actual = rangifflerUserDataClient
                .getFriends(user.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getList(".", UserJson.class);

        List<UserJson> expected = List.of(friend.setFriendStatus(FriendStatus.FRIEND.name()));

        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @AllureId("208")
    @DisplayName("/invitations Получение приглашений")
    void getInvitationsUser(@UserData(username = "myUser208") UserJson user,
                            @UserData(username = "friend208") UserJson friend,
                            @UserData(username = "notFriend208") UserJson notFriend) {

        friendsHelper.inviteFriends(user, friend, FriendStatus.INVITATION_SENT);

        List<UserJson> actual = rangifflerUserDataClient
                .getInvitations(friend.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getList(".", UserJson.class);

        List<UserJson> expected = List.of(user.setFriendStatus(FriendStatus.INVITATION_RECEIVED.name()));

        AssertUtil.assertEquals(expected, actual);
    }
}
