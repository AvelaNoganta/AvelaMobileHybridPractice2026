package Base;

import Pages.LoginPage;
import Utilities.DriverFactory1;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jdk.jfr.Description;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    //The reason we use protected in BaseTest is about visibility and inheritance.
    //My test classes extend BaseTest, so they need access to those variables.
//Visibility: Protected members are accessible within the same package and by subclasses, even if they are in different packages.
// This allows any test class
// that extends BaseTest to access the driver, config, and loginPage without needing to create new instances or pass them around.
    protected AppiumDriver driver;
    protected Properties config;
    protected LoginPage loginPage;

    public void setUp() throws IOException {
        // Load config.properties
        config = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/resources/configs/config.properties"
        );
        config.load(fis);
        // Initialize driver
        DriverFactory1.initDriver(config);

        // Get driver instance
        driver = DriverFactory1.getDriver();

        // Initialize page
        loginPage = new LoginPage(driver, config);
    }

    @Description("This method is responsible for cleaning up after the test execution. It quits the driver session to free up resources and ensure that there are no lingering sessions that could interfere with subsequent tests.")
    @Severity(SeverityLevel.MINOR)
    public void tearDown() {

        // Quit driver
        DriverFactory1.quitDriver();
    }
}
