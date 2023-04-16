package rangiffler.generators;

import rangiffler.model.UserJson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;


public class UserDataGenerator {

    private static final String PATH = "data/foto.jpeg";

    public UserJson createDefaultUserData() {
        UserJson userJson = new UserJson();
        byte[] photo = getPhotoAsBase64StringFromClasspath();
        userJson.setUsername("username".concat(random(10000, 99999).toString()))
                .setLastName("lastName".concat(random(10000, 99999).toString()))
                .setFirstName("firstName".concat(random(10000, 99999).toString()))
                .setAvatar(photo != null && photo.length > 0 ? new String(photo, StandardCharsets.UTF_8) : null);
        return userJson;
    }

    private static Integer random(int min, int max) {
        max -= min;
        return ((int) (Math.random() * ++max) + min);
    }

    private byte[] getPhotoAsBase64StringFromClasspath() {
        ClassLoader classLoader = UserDataGenerator.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(PATH)) {
            return Base64.getEncoder().encode(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUTCdatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
