package rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rangiffler.model.UserJson;
import rangiffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static rangiffler.condition.PhotoCondition.photo;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();
    private final SelenideElement yourFriends = $("div[aria-label='Your friends']");
    private final SelenideElement yourPhoto = $("div[aria-label='Your photos']");
    private final SelenideElement yourVisitedCountries = $("div[aria-label='Your visited countries']");
    private final SelenideElement userInfo = $("svg[data-testid='PersonIcon']");
    private final SelenideElement firstNameInput = $("input[name='firstName']");
    private final SelenideElement lastNameInput = $("input[name='lastName']");
    private final SelenideElement saveButton = $(byText("Save"));
    private final SelenideElement buttonAddPhoto = $(byText("Add photo"));
    private final SelenideElement yorFriendPhoto = $(byText("Friends travels"));
    private final SelenideElement editPhoto = $("svg[data-testid='EditIcon']");


    public Header getHeader() {
        return header;
    }

    @Step("Check that page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Rangiffler"));
        return this;
    }

    @Step("Check that page yourFriendsOpen")
    public MainPage yourFriendsOpen() {
        yourFriends.click();
        return this;
    }

    @Step("Check yourFriendsPhoto")
    public MainPage yourFriendsPhoto(String photoPath) {
        yorFriendPhoto.click();
        $("img[class=photo__list-item]").shouldHave(photo(photoPath));
        return this;
    }

    @Step("Check yourPhotoCounts")
    public MainPage yourPhotoCounts(String count) {
        yourPhoto.shouldHave(text(count));
        return this;
    }

    @Step("Check yourVisitedCountriesCounts")
    public MainPage yourVisitedCountriesCounts(String count) {
        yourVisitedCountries.shouldHave(text(count));
        return this;
    }

    @Step("Open userInfo")
    public MainPage userInfoOpen() {
        userInfo.click();
        return this;
    }

    @Step("Set firstName: {0}")
    public MainPage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }

    @Step("Set lastName: {0}")
    public MainPage setLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    @Step("Save profile")
    public MainPage saveProfile() {
        saveButton.click();
        return this;
    }

    @Step("Add photo")
    public MainPage addPhoto(String photoPath) {
        buttonAddPhoto.click();
        $(".visually-hidden[type=file]").uploadFromClasspath(photoPath);
        saveButton.click();
        $("img[class=photo__list-item]").shouldHave(photo(photoPath));
        return this;
    }

    @Step("Edit photo")
    public MainPage editPhoto(String description, String oldDescription) {
        $("img[class=photo__list-item]").click();
        editPhoto.click();
        $(byText(oldDescription)).setValue(description);
        saveButton.click();
        return this;
    }

    @Step("Update avatar with img: {avatarPath}")
    public MainPage updateAvatar(String avatarPath) {
        $(".visually-hidden[type=file]").uploadFromClasspath(avatarPath);
        return this;
    }

    @Step("Check photo")
    public void checkPhoto(String photoPath, UserJson user) {
        String selector = String.format("img[alt='%s']", user.getUsername());
        $(selector).shouldHave(photo(photoPath));
    }


}
