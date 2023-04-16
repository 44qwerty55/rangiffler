package rangiffler.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import rangiffler.model.UserJson;

import static io.restassured.RestAssured.given;

public class RangifflerUserDataClient {

    public ValidatableResponse postUpdateUser(UserJson body) {
        return given()
                .spec(requestSpec())
                .body(body)
                .post("/updateUser")
                .then();
    }

    public ValidatableResponse getCurrentUserOrCreate(String username) {
        return given()
                .spec(requestSpec())
                .queryParams("username", username)
                .get("/currentUser")
                .then();
    }

    public ValidatableResponse getAllUsers() {
        return given()
                .spec(requestSpec())
                .get("/users")
                .then();
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8092")
                .build();
    }
}
