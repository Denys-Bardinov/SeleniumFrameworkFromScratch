package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.NetworkWaiter;
import org.openqa.selenium.By;

public class LoginPage {

    private ActionDriver actionDriver;
    private final NetworkWaiter networkWaiter = BaseClass.getNetworkWaiter();


    //Define locators using By class

    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[text()=' Login ']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    public LoginPage() {
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Method to perform login
    public void login(String userName, String password) {
        networkWaiter.reset();
        actionDriver.enterText(userNameField, userName);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
        networkWaiter.waitForAllNetworkRequestsToFinish(java.time.Duration.ofSeconds(10));
    }

    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to get text from error message
    public String getErrorMessageTest() {
        return actionDriver.getText(errorMessage);
    }

    //Verify if error message is displayed correct or not
    public boolean verifyErrorMessage(String expectedError) {
        return actionDriver.compareText(errorMessage, expectedError);
    }

}
