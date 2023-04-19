package rangiffler.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.jsonunit.JsonPatchMatcher;
import io.restassured.response.ValidatableResponse;
import lombok.experimental.UtilityClass;
import net.javacrumbs.jsonunit.core.Option;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class AssertUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(AssertUtil.class);

    public static void assertEquals(Object expected, Object actual, String... ignorable) {
        String expectedNode = objectMapper.valueToTree(expected).toPrettyString();
        String actualNode = objectMapper.valueToTree(actual).toPrettyString();
        if (!JsonPatchMatcher.jsonEquals(expectedNode)
                .whenIgnoringPaths(ignorable).when(Option.IGNORING_ARRAY_ORDER).matches(actualNode)) {
            throw new AssertionFailedError("Ожидаемый ответ не совпадает с фактическим expected " + expectedNode + " actual " + actualNode);
        }
    }

    public static void assertEquals(Object expected, ValidatableResponse actual, String... ignorable) {
        String expectedNode = objectMapper.valueToTree(expected).toPrettyString();
        String actualNode = actual.extract().body().jsonPath().prettify();
        if (!JsonPatchMatcher.jsonEquals(expectedNode)
                .whenIgnoringPaths(ignorable).when(Option.IGNORING_ARRAY_ORDER).matches(actualNode)) {
            throw new AssertionFailedError("Ожидаемый ответ не совпадает с фактическим expected " + expectedNode + " actual " + actualNode);
        }
    }

}
