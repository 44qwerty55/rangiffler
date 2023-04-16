package rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.*;
import rangiffler.api.RangifflerAuthClient;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.config.Config;
import rangiffler.jupiter.annotation.ApiLogin;
import rangiffler.jupiter.annotation.GenerateUser;
import rangiffler.jupiter.annotation.User;
import rangiffler.model.UserJson;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static rangiffler.utils.DataUtils.generateRandomPassword;
import static rangiffler.utils.DataUtils.generateRandomUsername;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final RangifflerAuthClient authClient = new RangifflerAuthClient();
    private final RangifflerUserDataClient rangifflerUserDataClient = new RangifflerUserDataClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.NESTED);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<Selector, GenerateUser> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<Selector, GenerateUser> entry : userAnnotations.entrySet()) {
            String username = entry.getValue().username();
            String password = entry.getValue().password();
            if ("".equals(username)) {
                username = generateRandomUsername();
            }
            if ("".equals(password)) {
                password = generateRandomPassword();
            }
            UserJson userJson = apiRegister(username, password);
            context.getStore(entry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId, UserJson.class);
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    public enum Selector {
        METHOD, NESTED;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }
                case NESTED -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }
                default -> {
                    throw new IllegalStateException();
                }
            }
        }
    }

    private Map<Selector, GenerateUser> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<Selector, GenerateUser> annotationsOnTest = new HashMap<>();
        GenerateUser annotationOnMethod = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (annotationOnMethod != null && annotationOnMethod.handleAnnotation()) {
            annotationsOnTest.put(Selector.METHOD, annotationOnMethod);
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.rangifflerUser().handleAnnotation()) {
            annotationsOnTest.put(Selector.NESTED, apiLoginAnnotation.rangifflerUser());
        }
        return annotationsOnTest;
    }

    private UserJson apiRegister(String username, String password) throws Exception {
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserJson currentUser = rangifflerUserDataClient.getCurrentUserOrCreate(username).assertThat()
                .statusCode(HttpStatus.SC_OK).extract().as(UserJson.class);
        currentUser.setPassword(password);
        return currentUser;
    }
}
