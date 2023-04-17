package rangiffler.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import rangiffler.model.FriendsNameForFoto;
import rangiffler.model.PhotoJson;

import static io.restassured.RestAssured.given;

public class RangifflerPhotoClient {

    public ValidatableResponse postAddPhoto(PhotoJson body) {
        return given()
                .spec(requestSpec())
                .body(body)
                .post("/addPhoto")
                .then();
    }

    public ValidatableResponse postEditPhoto(PhotoJson body) {
        return given()
                .spec(requestSpec())
                .body(body)
                .post("/editPhoto")
                .then();
    }


    public ValidatableResponse postFriendsPhoto(FriendsNameForFoto friends) {
        return given()
                .spec(requestSpec())
                .body(friends)
                .post("/friends/photos")
                .then();
    }

    public ValidatableResponse getListUserPhotos(String username) {
        return given()
                .spec(requestSpec())
                .queryParams("username", username)
                .get("/photos")
                .then();
    }

    public ValidatableResponse deletePhoto(String photoUuid) {
        return given()
                .spec(requestSpec())
                .queryParams("photoUuid", photoUuid)
                .delete("/dellPhoto")
                .then();
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8093")
                .build();
    }
}
