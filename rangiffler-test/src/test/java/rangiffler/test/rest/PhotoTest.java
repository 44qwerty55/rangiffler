package rangiffler.test.rest;

import io.qameta.allure.AllureId;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.api.RangifflerPhotoClient;
import rangiffler.data.dao.PhotoDao;
import rangiffler.generators.PhotoGenerator;
import rangiffler.jupiter.annotation.DAO;
import rangiffler.jupiter.annotation.UserPhoto;
import rangiffler.jupiter.extension.DAOResolverPhoto;
import rangiffler.jupiter.extension.UsersPhotoExtension;
import rangiffler.model.FriendsNameForFoto;
import rangiffler.model.PhotoJson;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import java.util.List;

import static rangiffler.model.Country.FIJI;
import static rangiffler.model.Country.IRAN;

@ExtendWith({DAOResolverPhoto.class, UsersPhotoExtension.class})
public class PhotoTest extends BaseTest {

    private final RangifflerPhotoClient rangifflerPhotoClient = new RangifflerPhotoClient();
    @DAO
    private PhotoDao photoDao;

    @Test
    @AllureId("100")
    @DisplayName("/addPhoto Проверка добавления фото")
    void addPhoto() {
        PhotoJson expected = new PhotoGenerator().createDefaultPhoto(FIJI);
        ValidatableResponse actual = rangifflerPhotoClient
                .postAddPhoto(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(expected, actual, "id");
    }

    @Test
    @AllureId("101")
    @DisplayName("/dellPhoto Проверка удаления фото")
    void deletePhoto(@UserPhoto() PhotoJson expected) {
        rangifflerPhotoClient
                .deletePhoto(expected.getId().toString())
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED);

        Assertions.assertNull(photoDao.getPhoto(expected.getId()));
    }

    @Test
    @AllureId("102")
    @DisplayName("/editPhoto Проверка обновления фото")
    void updatePhoto(@UserPhoto() PhotoJson expected) {
        expected.setDescription("New description");

        ValidatableResponse actual = rangifflerPhotoClient
                .postEditPhoto(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
        AssertUtil.assertEquals(expected, actual);
    }

    @Test
    @AllureId("103")
    @DisplayName("/photos Проверка получения списка фото")
    void getPhoto(@UserPhoto(username = "dima88899", country = IRAN) PhotoJson expected) {

        List<PhotoJson> actual = rangifflerPhotoClient
                .getListUserPhotos(expected.getUsername())
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList(".", PhotoJson.class);

        AssertUtil.assertEquals(List.of(expected), actual);
    }

    @Test
    @AllureId("104")
    @DisplayName("/friends/photos Проверка получения списка фото")
    void getFriendPhoto(@UserPhoto() PhotoJson expected) {
        List<PhotoJson> actual = rangifflerPhotoClient
                .postFriendsPhoto(new FriendsNameForFoto(List.of(expected.getUsername())))
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().jsonPath().getList(".", PhotoJson.class);

        AssertUtil.assertEquals(List.of(expected), actual);
    }

}
