package rangiffler.test.rest;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.data.dao.UserDataDao;
import rangiffler.generators.UserDataGenerator;
import rangiffler.jupiter.annotation.DAO;
import rangiffler.jupiter.extension.DAOResolverUserData;
import rangiffler.model.UserJson;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import java.util.UUID;

@ExtendWith({DAOResolverUserData.class})
public class UserDataTest extends BaseTest {

    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();
    @DAO
    private UserDataDao userDataDao;
    private UserJson expected;


    @BeforeEach
    void create() {
        expected = new UserDataGenerator().createDefaultUserData();
    }

    @AfterEach
    void dell() {
        if (expected.getId() != null) {
            userDataDao.deleteUser(expected.getUsername());
        }
    }


    @Test
    @DisplayName("/currentUser Проверка добавления пользователя")
    void addUser() {
        ValidatableResponse actual = rangifflerUserDataClient
                .getCurrentUserOrCreate(expected.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        expected = UserJson.fromEntity(userDataDao.getUser(expected.getUsername()));
        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("/updateUser Проверка обновления данных пользователя")
    void updateUser() {
        String userId = rangifflerUserDataClient
                .getCurrentUserOrCreate(expected.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().path("id");
        expected.setId(UUID.fromString(userId));

        ValidatableResponse actual = rangifflerUserDataClient
                .postUpdateUser(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(expected, actual);
    }

}
