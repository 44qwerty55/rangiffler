package rangiffler.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RangifflerCountryClient {

    public ValidatableResponse getCountries() {
        return given()
                .spec(requestSpec())
                .get("/countries")
                .then();
    }

    public ValidatableResponse getCountry(String code) {
        return given()
                .spec(requestSpec())
                .queryParams("code", code)
                .get("/country")
                .then();
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8091")
                .build();
    }
}
