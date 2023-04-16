package rangiffler.test.rest;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.api.RangifflerCountryClient;
import rangiffler.data.dao.CountriesDao;
import rangiffler.jupiter.annotation.DAO;
import rangiffler.jupiter.extension.DAOResolverGeo;
import rangiffler.model.CountriesJson;
import rangiffler.test.BaseTest;
import rangiffler.utils.AssertUtil;

import java.util.List;

import static rangiffler.model.Country.FIJI;

@ExtendWith({DAOResolverGeo.class})
public class CountriesTest extends BaseTest {

    private final RangifflerCountryClient rangifflerCountryClient = new RangifflerCountryClient();
    @DAO
    private CountriesDao countriesDao;
    private int COUNT_COUNTRIES = 175;


    @Test
    @DisplayName("/countries Проверка кол-ва стран")
    void getCountries() {
        Awaitility.await().untilAsserted(() -> {
            List<CountriesJson> actualList = rangifflerCountryClient
                    .getCountries()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", CountriesJson.class);
            Assertions.assertEquals(actualList.size(), COUNT_COUNTRIES);
        });
    }

    @Test
    @DisplayName("/country Проверка вывода отдельной страны")
    void getCountry() {
        ValidatableResponse actual = rangifflerCountryClient
                .getCountry(FIJI.getCode())
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        CountriesJson expected = CountriesJson
                .fromEntity(countriesDao.getCountryByCode(FIJI.getCode()));
        AssertUtil.assertEquals(expected, actual);
    }
}
