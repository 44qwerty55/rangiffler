package rangiffler.test.rest;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.api.RangifflerPhotoClient;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.data.dao.PhotoDao;
import rangiffler.data.dao.UserDataDao;
import rangiffler.generators.PhotoGenerator;
import rangiffler.generators.UserDataGenerator;
import rangiffler.jupiter.annotation.DAO;
import rangiffler.jupiter.extension.DAOResolverPhoto;
import rangiffler.jupiter.extension.DAOResolverUserData;
import rangiffler.model.PhotoJson;
import rangiffler.model.UserJson;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import java.util.UUID;

import static rangiffler.model.Country.TANZANIA;

@ExtendWith({DAOResolverPhoto.class})
public class PhotoTest extends BaseTest {

    private final RangifflerPhotoClient rangifflerPhotoClient = new RangifflerPhotoClient();
    @DAO
    private PhotoDao photoDao;
    private PhotoJson expected;


    @BeforeEach
    void create() {
        expected = new PhotoGenerator().createDefaultPhoto(TANZANIA);
    }

    @AfterEach
    void dell() {
            photoDao.deleteUserPhoto(expected.getUsername());
    }


    @Test
    @DisplayName("/addPhoto Проверка добавления фото")
    void addPhoto() {
        ValidatableResponse actual = rangifflerPhotoClient
                .postAddPhoto(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        AssertUtil.assertEquals(expected, actual , "id");
    }

}
