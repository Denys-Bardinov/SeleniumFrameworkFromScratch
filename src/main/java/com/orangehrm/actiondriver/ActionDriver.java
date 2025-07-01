package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActionDriver {

    protected ActionDriver actionDriver;
    private WebDriver driver;
    private WebDriverWait wait;

    public static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(BaseClass.getProperties().getProperty("explicitWait"))));
    }

    //Method to click element
    public void click(By by) {
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("clicked an element --->" + elementDescription);
        } catch (Exception e) {
            logger.info("Unable to click  an element " + e.getMessage());
        }
    }

    //Method to enter text into an input field
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text on: " + getElementDescription(by) + " " + value);
        } catch (Exception e) {
            logger.info("Unable to enter the value in input box " + e.getMessage());
        }
    }

    //Method to get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get the text " + e.getMessage());
            return "";
        }
    }

    // Compare two text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                logger.info("Text are matching: " + actualText + " equals " + expectedText);
                return true;
            } else {
                logger.error("Text are not matching: " + actualText + " not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare text " + e.getMessage());
        }
        return false;
    }

    //Method to check if element is displayed
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            boolean isDisplayed = driver.findElement(by).isDisplayed();
            logger.info("Element is displayed " + getElementDescription(by));
            if (isDisplayed) {
                logger.info("Element is visible");
                return isDisplayed;
            } else {
                return isDisplayed;
            }
        } catch (Exception e) {
            logger.error("Element is not displayed");
            return false;
        }
    }

    //Scroll to element
    public void scrollToElement(By by) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("argument[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            logger.error("Unable to locate an element " + e.getMessage());
        }

    }

    //Wait for element to be clickable
    private void waitForElementToBeClickable(By by) {
        try {
            waitForElementToBeVisible(by);
            wait.until((ExpectedConditions.elementToBeClickable(by)));
        } catch (Exception e) {
            logger.error("Element is not clickable " + e.getMessage());
        }
    }

    // Wait for element to be visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible " + e.getMessage());
        }
    }

    //Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        //Check for null driver or locator to avoid NullPointerExeption
        if (driver == null) {
            return "driver is null";
        }

        if (locator == null) {
            return "locator is null";
        }

        try {
            WebElement element = driver.findElement(locator);

            Map<String, String> attributes = new LinkedHashMap<>();
            attributes.put("name", element.getDomAttribute("name"));
            attributes.put("id", element.getDomAttribute("id"));
            attributes.put("text", element.getText());
            attributes.put("class", element.getDomAttribute("class"));
            attributes.put("placeholder", element.getDomAttribute("placeholder"));

            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (isNotEmpty(value)) {
                    if ("text".equals(key)) {
                        return "Element with text: " + truncateString(value, 20);
                    }
                    return "Element with " + key + ": " + value;
                }
            }

        } catch (Exception e) {
            logger.error("Failed to describe element: {}", e.getMessage(), e);
        }

        return "Unable to describe the element";
    }

    //Utility method to check String is not null or Empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    //Utility method to truncate string
    private String truncateString(String value, int maxLenght) {
        if (value == null || value.length() < maxLenght) {
            return value;
        }
        return value.substring(0, maxLenght) + "...";
    }


}
