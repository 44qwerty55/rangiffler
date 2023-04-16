package rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;
import rangiffler.jupiter.annotation.UserLogin;
import rangiffler.model.UserJson;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UsersListExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersListExtension.class);


    private static final Queue<UserJson> USER_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USER_QUEUE.add(new UserJson("dima", "1234"));
        USER_QUEUE.add(new UserJson("qwerty", "1234"));
        USER_QUEUE.add(new UserJson("qwerty1", "1234"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        List<UserLogin> userLogins = new ArrayList<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .forEach(it -> {
                    if (it.isAnnotationPresent(UserLogin.class)) {
                        userLogins.add(it.getAnnotation(UserLogin.class));
                    }
                });

        List<UserJson> userJsons = new ArrayList<>();

        userLogins.forEach(it -> {
            UserJson user = null;
            while (user == null) {
                user = USER_QUEUE.poll();
                if (user != null) {
                    userJsons.add(user);
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
            us.setAvailable(true);
            USER_QUEUE.add(us);
        });
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(UserLogin.class);
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
        throw new RuntimeException("No user found");
    }

}