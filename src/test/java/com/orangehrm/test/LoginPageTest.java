package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();
    }

    @Test
    public void verifyLoginTest() {
        loginPage.login("Admin", "admin123");
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
