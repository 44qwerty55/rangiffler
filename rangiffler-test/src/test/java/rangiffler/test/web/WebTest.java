package rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import rangiffler.jupiter.annotation.ApiLogin;
import rangiffler.page.MainPage;
import rangiffler.test.BaseTest;

@Execution(ExecutionMode.SAME_THREAD)
public class WebTest extends BaseTest {

    @Test
    @AllureId("50")
    @ResourceLock("spend")
    @ApiLogin(username = "dima", password = "1234")
    void checkLastWeekSpendingTest() throws Exception {


        Selenide.open(MainPage.URL, MainPage.class);
             //   .getSpendingTable()
             //   .clickByButton("Last week")
             //   .checkTableContains(expected);

        Thread.sleep(2000);
    }

}
