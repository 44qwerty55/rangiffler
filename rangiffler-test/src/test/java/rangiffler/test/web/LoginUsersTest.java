package rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.jupiter.annotation.UserLogin;
import rangiffler.jupiter.extension.UsersListExtension;
import rangiffler.model.UserJson;
import rangiffler.test.BaseTest;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;



@ExtendWith({UsersListExtension.class})
public class LoginUsersTest extends BaseTest {

    @AllureId("1")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(@UserLogin() UserJson userAdmin,
                                                    @UserLogin() UserJson userCommonFirst,
                                                    @UserLogin() UserJson userCommonSecond) {
        List<UserJson> users = List.of(userAdmin, userCommonFirst, userCommonSecond);
        users.forEach(data -> {
            System.out.println("#### Test 1 " + data.toString());
            Allure.step("Check login", () -> login(data));
        });
    }

    @AllureId("2")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin0(@UserLogin() UserJson user,
                                                     @UserLogin() UserJson userCommon) {
        List<UserJson> users = List.of(user, userCommon);
        users.forEach(data -> {
        System.out.println("#### Test 2 " + user.toString());
        Allure.step("Check login", () -> login(user));
        });
    }

    @AllureId("3")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin1(@UserLogin() UserJson user) {
        System.out.println("#### Test 3 " + user.toString());
        Allure.step("Check login", () -> login(user));
    }

    @AllureId("4")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin4(@UserLogin() UserJson user) {
        System.out.println("#### Test 4 " + user.toString());
        Allure.step("Check login", () -> login(user));
    }

    private void login(UserJson user) {
        Selenide.open("http://127.0.0.1:3001/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();
        Assertions.assertEquals( "Login to Rangiffler", title());
        Selenide.closeWebDriver();
    }
}
