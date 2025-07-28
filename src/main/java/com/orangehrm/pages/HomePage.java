package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.NetworkWaiter;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage {
    private final NetworkWaiter networkWaiter = BaseClass.getNetworkWaiter();
    private ActionDriver actionDriver;

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
    private By pimTab = By.xpath("//span[text()='PIM']");
    private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By employeeFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
    private By employeeLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

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

    // Method to navigate to PIM tab
    public void clickOnPIMTab() {
        actionDriver.click(pimTab);
        networkWaiter.waitForAllNetworkRequestsToFinish(Duration.ofDays(10));
    }

    //Employee search
    public void setEmployeeSearch(String value) {
        actionDriver.enterText(employeeSearch, value);
        actionDriver.click(searchButton);
        actionDriver.scrollToElement(employeeFirstAndMiddleName);
    }

    public boolean verifyEmployeeFirstAndMiddleName(String employeeFirstAndMiddleNameFromDB) {
       return actionDriver.compareText(employeeFirstAndMiddleName, employeeFirstAndMiddleNameFromDB);
    }

    public boolean verifyEmployeeLastName(String employeeLastNameFromDB) {
        return actionDriver.compareText(employeeLastName, employeeLastNameFromDB);
    }

    //Method to perform logout operation
    public void logout() {
        networkWaiter.reset();
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
        networkWaiter.waitForAllNetworkRequestsToFinish(java.time.Duration.ofSeconds(10));
    }



}
