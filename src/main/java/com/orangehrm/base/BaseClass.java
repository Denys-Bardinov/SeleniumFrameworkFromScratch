package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.page.Page;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties properties;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeMethod
    public void setup() {
        System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        logger.info("WebDriver initialized and browser maximised");

        //Initialise ActionDriver for the current Thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for Thread: " + Thread.currentThread().getName());
    }

    //Load the configuration file
    @BeforeSuite
    public void loadConfig() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            logger.info("config.properties file loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Initialize WebDriver based on properties file
    private void launchBrowser() {
        String browser = properties.getProperty("browser");

        ChromeOptions options = new ChromeOptions();
// НЕ добавляй headless
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        if (browser.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
            driver.set(new ChromeDriver(options));
        } else if (browser.equalsIgnoreCase("firefox")) {
//            driver = new FirefoxDriver();
            driver.set(new FirefoxDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
//            driver = new EdgeDriver();
            driver.set(new EdgeDriver());

        } else {
            throw new IllegalArgumentException("Browser not supported");
        }
    }

    private void configureBrowser() {
        // Implicit Wait
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Maximize the browser
        getDriver().manage().window().maximize();

        //Navigate to URL
        getDriver().get(properties.getProperty("url"));
    }


    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
        }
        logger.info("WebDriver instance is closed");
//        driver = null;
//        actionDriver = null;
        driver.remove();
        actionDriver.remove();
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("WebDriver is not initialised");
            throw new IllegalStateException("WebDriver is not initialised");
        }
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("ActionDriver is not initialised");
            throw new IllegalStateException("ActionDriver is not initialised");
        }
        return actionDriver.get();
    }

    public static void setDriver(ThreadLocal<WebDriver> driver) {
        BaseClass.driver = driver;
    }

    public static Properties getProperties() {
        return properties;
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
