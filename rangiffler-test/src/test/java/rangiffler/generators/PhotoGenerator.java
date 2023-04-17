package rangiffler.generators;

import rangiffler.data.dao.CountriesDao;
import rangiffler.data.dao.PostgresJdbcGeoDAO;
import rangiffler.model.CountriesJson;
import rangiffler.model.Country;
import rangiffler.model.PhotoJson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class PhotoGenerator {

    private static final String PATH = "data/foto.jpeg";
    private static final CountriesDao countriesDao = new PostgresJdbcGeoDAO();

    public PhotoJson createDefaultPhoto(Country country) {
        PhotoJson photoJson = new PhotoJson();
        byte[] photo = getPhotoAsBase64StringFromClasspath();
        photoJson.setUsername("username".concat(random(10000, 99999).toString()))
                .setDescription("Description".concat(random(10000, 99999).toString()))
                .setPhoto(photo != null && photo.length > 0 ? new String(photo, StandardCharsets.UTF_8) : null)
                .setCountryJson(CountriesJson.fromEntity(countriesDao.getCountryByCode(country.getCode())));
        return photoJson;
    }

    private static Integer random(int min, int max) {
        max -= min;
        return ((int) (Math.random() * ++max) + min);
    }

    private byte[] getPhotoAsBase64StringFromClasspath() {
        ClassLoader classLoader = PhotoGenerator.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(PATH)) {
            return Base64.getEncoder().encode(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
