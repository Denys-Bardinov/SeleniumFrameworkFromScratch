package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPageDataFromFileTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();
    }

    @Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
    public void verifyLoginTest(String userName, String password) {
        loginPage.login(userName, password);
        Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login");
        homePage.logout();
    }

    @Test
    public void verifyInavalidLoginTest() {
        loginPage.login("abcd", "abcd");
        staticWait(3);
        Assert.assertTrue(loginPage.verifyErrorMessage("Invalid credentials"), "Test failed: invalid error message");
    }
}
