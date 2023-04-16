package rangiffler.test;

import io.qameta.allure.junit5.AllureJunit5;


import org.junit.jupiter.api.extension.ExtendWith;
import rangiffler.config.Config;
import rangiffler.jupiter.extension.BeforeSuiteExtension;
import rangiffler.jupiter.extension.BrowserExtension;

@ExtendWith({BrowserExtension.class, AllureJunit5.class, BeforeSuiteExtension.class})
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();
}
