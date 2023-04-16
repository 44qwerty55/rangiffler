package rangiffler.jupiter.annotation;

import rangiffler.jupiter.extension.ApiAuthExtension;
import rangiffler.jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateUserExtension.class, ApiAuthExtension.class})
public @interface ApiLogin {

    String username() default "";

    String password() default "";

    GenerateUser rangifflerUser() default @GenerateUser(handleAnnotation = false);
}
