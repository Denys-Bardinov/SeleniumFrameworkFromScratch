package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        this.loginPage = new LoginPage();
        this.homePage = new HomePage();
    }

    @Test
    public void verifyEmployeeNameFromDB() {
        loginPage.login("admin", "Adminadmin1&");
        homePage.clickOnPIMTab();
        homePage.setEmployeeSearch("Igor");

        SoftAssert softAssert = getSoftAssert();

        String employee_id = "2";

        Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

        String emplFirstName = employeeDetails.get("firstName");
        String emplMiddleName = employeeDetails.get("middleName");
        String emplLastName = employeeDetails.get("lastName");

        String empFirstAndMiddleName = (emplFirstName + " + " + emplMiddleName).trim();

        softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(empFirstAndMiddleName), "First and Middle names are not matching");
        Assert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));

        softAssert.assertAll();
    }


}
