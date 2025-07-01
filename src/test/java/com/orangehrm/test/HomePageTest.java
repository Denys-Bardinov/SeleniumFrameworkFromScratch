package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();
    }

    @Test
    public void verifyOrangeHRMLogo() {
        loginPage.login("Admin", "admin123");
        staticWait(3);
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");
    }
}
