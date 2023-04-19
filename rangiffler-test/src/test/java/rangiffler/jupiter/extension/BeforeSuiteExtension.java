package rangiffler.jupiter.extension;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.config.DecoderConfig.decoderConfig;

public class BeforeSuiteExtension implements AroundAllTestsExtension {

    @Override
    public void beforeAllTests(ExtensionContext context) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().defaultContentCharset(StandardCharsets.UTF_8));
        RestAssured.filters(
                List.of(new AllureRestAssured(), new RequestLoggingFilter(), new ResponseLoggingFilter()));
        Awaitility.setDefaultPollDelay(1, TimeUnit.SECONDS);
        Awaitility.setDefaultPollInterval(5, TimeUnit.SECONDS);
        Awaitility.setDefaultTimeout(60, TimeUnit.SECONDS);


        Configuration.browserSize = "1920x1080";
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        desiredCapabilities.setAcceptInsecureCerts(true);
        Configuration.browserCapabilities = desiredCapabilities;
    }

    @Override
    public void afterAllTests() {
        System.out.println("AFTER SUITE!");
    }
}
