package Tests;

import Base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import jdk.jfr.Description;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Description("This is a setup test to initialize the driver and load the app or website before executing the login test.")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void SetUp() throws Exception {
        setUp();
    }
@Description("Login test that performs the login action on the Ndosi Automation app or website using credentials from the config.properties file. It clicks the burger menu, navigates to the sign-in page, enters email and password, and submits the login form.")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "SetUp")
    public void LoginToNdosiAutomation() {
        loginPage.clickBurgerMenuButton();
        loginPage.clickSignInButton();
        loginPage.enterEmail(config.getProperty("email"));
        loginPage.enterPassword(config.getProperty("password"));
        loginPage.clickLoginButton();
    }

    @Test(dependsOnMethods = "LoginToNdosiAutomation")
    public void tearDown() {
        super.tearDown();
    }


}
