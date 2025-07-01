package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;

public class HomePage {

    private ActionDriver actionDriver;

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    public HomePage() {
        this.actionDriver = BaseClass.getActionDriver();
    }

    //Method To Verify If Admin Tab Is Visible
    public boolean isAdminTabVisible() {
        return actionDriver.isDisplayed(adminTab);
    }

    //Method To Verify If orange tab is visible
    public boolean verifyOrangeHRMLogo() {
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    //Method to perform logout operation
    public void logout() {
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
    }



}
