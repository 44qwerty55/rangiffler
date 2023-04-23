package rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.jupiter.annotation.UserLogin;
import rangiffler.jupiter.extension.UsersListExtension;
import rangiffler.model.UserJson;
import rangiffler.test.BaseTest;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;


@ExtendWith({UsersListExtension.class})
public class LoginUsersTest extends BaseTest {

    @AllureId("1")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(@UserLogin() UserJson user) {
        System.out.println("#### Test 1 " + user.toString());
        Selenide.open("http://127.0.0.1:3001/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();
        Assertions.assertEquals("Login to Rangiffler", title());
        Selenide.closeWebDriver();
    }
}