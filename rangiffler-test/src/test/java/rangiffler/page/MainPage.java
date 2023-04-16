package rangiffler.page;

import io.qameta.allure.Step;
import rangiffler.page.component.Footer;
import rangiffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();
    protected final Footer footer = new Footer();

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    @Step("Check that page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Rangiffler"));
     return this;
    }
}
