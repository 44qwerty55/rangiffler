package rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.*;
import rangiffler.api.RangifflerUserDataClient;
import rangiffler.data.dao.PostgresJdbcUserDataDAO;
import rangiffler.jupiter.annotation.UserData;
import rangiffler.model.UserJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static rangiffler.utils.DataUtils.generateRandomUsername;

public class UsersDataExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersDataExtension.class);

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        List<UserData> userDatas = new ArrayList<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .forEach(it -> {
                    if (it.isAnnotationPresent(UserData.class)) {
                        userDatas.add(it.getAnnotation(UserData.class));
                    }
                });

        List<UserJson> userJsons = new ArrayList<>();

        userDatas.forEach(it -> {
            UserJson user = null;
            while (user == null) {
                String username = it.username();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                user = new RangifflerUserDataClient()
                        .getCurrentUserOrCreate(username)
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK).extract().as(UserJson.class);
                if (user != null) {
                    userJsons.add(user.setAvailable(true));
                }
            }
        });
        context.getStore(NAMESPACE).put(id, userJsons);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        List<UserJson> users = context.getStore(NAMESPACE).get(id, List.class);
        users.forEach(us -> {
                    new PostgresJdbcUserDataDAO().deleteUserFriend(us.getId().toString());
                    new PostgresJdbcUserDataDAO().deleteUser(us.getUsername());
                }
        );
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(UserData.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String id = getTestId(extensionContext);
        List<UserJson> users = extensionContext.getStore(NAMESPACE).get(id, List.class);
        for (UserJson user : users) {
            if (!user.isAvailable())
                continue;

            user.setAvailable(false);
            return user;
        }
        throw new RuntimeException("No PhotoJson found");
    }

}