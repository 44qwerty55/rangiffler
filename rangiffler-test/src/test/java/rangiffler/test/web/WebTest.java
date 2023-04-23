package rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.data.dao.PostgresJdbcPhotoDAO;
import rangiffler.data.dao.UserDataDao;
import rangiffler.data.entity.PhotoEntity;
import rangiffler.data.entity.UserDataEntity;
import rangiffler.jupiter.annotation.*;
import rangiffler.jupiter.extension.DAOResolverUserData;
import rangiffler.model.UserJson;
import rangiffler.page.MainPage;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith({DAOResolverUserData.class})
public class WebTest extends BaseTest {

    @DAO
    private UserDataDao userDataDao;

    @Test
    @AllureId("500")
    @ApiLogin(rangifflerUser = @GenerateUser(photos = @GeneratePhoto, friend = @GenerateFriend))
    void checkYouFriendTest(@User UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .yourFriendsOpen();
        $$("table[aria-label='friends table']").shouldHave(size(1));
        $("table[aria-label='friends table']", 0)
                .shouldBe(visible)
                .shouldHave(text(user.getUserFriend().getUsername()));
        //  Thread.sleep(1000);
    }

    @Test
    @AllureId("501")
    @ApiLogin(rangifflerUser = @GenerateUser(photos = @GeneratePhoto))
    void checkYouPhotoTest(@User UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .yourPhotoCounts("1");
    }

    @Test
    @AllureId("502")
    @ApiLogin(rangifflerUser = @GenerateUser(photos = @GeneratePhoto))
    void checkYouCountriesTest(@User UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .yourVisitedCountriesCounts("1");
    }

    @Test
    @AllureId("503")
    @ApiLogin(rangifflerUser = @GenerateUser())
    void updateUserTest(@User UserJson user) {
        user.setFirstName("testFirstName")
                .setLastName("testLastName");

        System.out.println("my user  " + user.getUsername());
        Selenide.open(MainPage.URL, MainPage.class)
                .userInfoOpen()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .saveProfile();

        Awaitility.await("Проверка данных пользователя " + user.getUsername()).untilAsserted(() -> {
            UserDataEntity userDataEntity = userDataDao.getUser(user.getUsername());
            AssertUtil.assertEquals(user, UserJson.fromEntity(userDataEntity));
        });
    }

    @Test
    @AllureId("504")
    @ApiLogin(rangifflerUser = @GenerateUser())
    void photoShouldBeAddToProfile(@User UserJson user) {
        String path = "data/foto.jpeg";
        Selenide.open(MainPage.URL, MainPage.class)
                .userInfoOpen()
                .updateAvatar(path)
                .saveProfile();
        Selenide.refresh();

        Selenide.open(MainPage.URL, MainPage.class)
                .checkPhoto(path, user);
    }

    @Test
    @AllureId("505")
    @ApiLogin(rangifflerUser = @GenerateUser())
    void photoShouldBeAddTo(@User UserJson user) {
        String path = "data/foto.jpeg";
        Selenide.open(MainPage.URL, MainPage.class)
                .addPhoto(path);
    }

    @Test
    @AllureId("506")
    @ApiLogin(rangifflerUser = @GenerateUser(photos = @GeneratePhoto, friend = @GenerateFriend))
    void checkYouFriendPhotoTest(@User UserJson user) {
        String path = "data/foto.jpeg";
        Selenide.open(MainPage.URL, MainPage.class)
                .yourFriendsPhoto(path);
    }

    @Test
    @AllureId("507")
    @ApiLogin(rangifflerUser = @GenerateUser(photos = @GeneratePhoto))
    void editYouPhotoTest(@User UserJson user) {
        String descriptions = "New description";

        Selenide.open(MainPage.URL, MainPage.class)
                .editPhoto(descriptions, user.getUserPhoto().getDescription());

        PhotoEntity photoEntity = new PostgresJdbcPhotoDAO().getPhoto(user.getUserPhoto().getId());
        Assertions.assertEquals(user.getUserPhoto().getDescription().concat(descriptions), photoEntity.getDescription());
        System.out.println(user.getUsername());
    }
}
