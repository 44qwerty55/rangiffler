package rangiffler.jupiter.annotation;


import rangiffler.jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@ExtendWith(CreateUserExtension.class)
public @interface GenerateUser {

    boolean handleAnnotation() default true;

    String username() default "";

    String password() default "";

    GeneratePhoto[] photos() default {};

    GenerateFriend[] friend() default {};

}
