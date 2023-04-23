package rangiffler.condition;
import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;


public class PhotoCondition {

    public static Condition photo(String path) {
        return new Condition(path) {

            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                String actualPhoto = element.getAttribute("src");
                byte[] actualBase64Photo = StringUtils.substringAfter(actualPhoto, "base64,").getBytes();
                byte[] expectedBase64Photo = getPhotoAsBase64StringFromClasspath();

                boolean valueMatches = Arrays.equals(expectedBase64Photo, actualBase64Photo);
                return new CheckResult(valueMatches, actualPhoto);
            }

            private byte[] getPhotoAsBase64StringFromClasspath() {
                ClassLoader classLoader = PhotoCondition.class.getClassLoader();
                try (InputStream is = classLoader.getResourceAsStream(path)) {
                    return Base64.getEncoder().encode(is.readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }




}
