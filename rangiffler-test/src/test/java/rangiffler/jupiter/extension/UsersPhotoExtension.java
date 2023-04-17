package rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.*;
import rangiffler.api.RangifflerPhotoClient;
import rangiffler.data.dao.PostgresJdbcPhotoDAO;
import rangiffler.generators.PhotoGenerator;
import rangiffler.jupiter.annotation.UserPhoto;
import rangiffler.model.Country;
import rangiffler.model.PhotoJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static rangiffler.utils.DataUtils.generateRandomUsername;

public class UsersPhotoExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersPhotoExtension.class);

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        List<UserPhoto> userPhotos = new ArrayList<>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .forEach(it -> {
                    if (it.isAnnotationPresent(UserPhoto.class)) {
                        userPhotos.add(it.getAnnotation(UserPhoto.class));
                    }
                });

        List<PhotoJson> photoJsons = new ArrayList<>();

        userPhotos.forEach(it -> {
            PhotoJson photo = null;
            while (photo == null) {
                String username = it.username();
                Country country = it.country();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                photo = new RangifflerPhotoClient()
                        .postAddPhoto(new PhotoGenerator().createDefaultPhoto(country).setUsername(username))
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK).extract().as(PhotoJson.class);
                if (photo != null) {
                    photoJsons.add(photo);
                }
            }
        });
        context.getStore(NAMESPACE).put(id, photoJsons);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        List<PhotoJson> photos = context.getStore(NAMESPACE).get(id, List.class);
        photos.forEach(us -> {
            new PostgresJdbcPhotoDAO().deleteUserPhoto(us.getUsername());
        });
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(PhotoJson.class)
                && parameterContext.getParameter().isAnnotationPresent(UserPhoto.class);
    }

    @Override
    public PhotoJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String id = getTestId(extensionContext);
        List<PhotoJson> users = extensionContext.getStore(NAMESPACE).get(id, List.class);
        for (PhotoJson user : users) {
            return user;
        }
        throw new RuntimeException("No PhotoJson found");
    }

}