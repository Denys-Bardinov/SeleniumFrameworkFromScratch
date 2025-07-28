package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import com.orangehrm.utilities.NetworkWaiter;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties properties;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    private static ThreadLocal<DevTools> devTools = new ThreadLocal<>();
    private static ThreadLocal<NetworkWaiter> networkWaiter = new ThreadLocal<>();
    protected ThreadLocal<SoftAssert> softAssertThreadLocal = ThreadLocal.withInitial(SoftAssert::new);

    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    public SoftAssert getSoftAssert() {
        return softAssertThreadLocal.get();
    }

    @BeforeMethod
    public synchronized void setup() throws IOException {
        System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        logger.info("WebDriver initialized and browser maximised");

        //Initialise ActionDriver for the current Thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for Thread: " + Thread.currentThread().getName());
    }

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
    private synchronized void launchBrowser() {
        String browser = properties.getProperty("browser");


        if (browser.equalsIgnoreCase("chrome")) {
            ChromeDriver chromeDriver = new ChromeDriver();
            driver.set(chromeDriver);

            // Инициализация DevTools
            DevTools dt = chromeDriver.getDevTools();
            dt.createSession();
            devTools.set(dt);

            // Инициализация NetworkWaiter
            NetworkWaiter waiter = new NetworkWaiter(dt);
            waiter.startTracking();
            networkWaiter.set(waiter);

        } else if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());

        } else if (browser.equalsIgnoreCase("edge")) {
            driver.set(new EdgeDriver());

        } else {
            throw new IllegalArgumentException("Browser not supported");
        }
    }

    private void configureBrowser() {
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().window().maximize();
        getDriver().get(properties.getProperty("url"));
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
        }
        logger.info("WebDriver instance is closed");
        driver.remove();
        actionDriver.remove();
        devTools.remove();
        networkWaiter.remove();
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

    public static DevTools getDevTools() {
        if (devTools.get() == null) {
            throw new IllegalStateException("DevTools is not initialized");
        }
        return devTools.get();
    }

    public static NetworkWaiter getNetworkWaiter() {
        if (networkWaiter.get() == null) {
            throw new IllegalStateException("NetworkWaiter is not initialized");
        }
        return networkWaiter.get();
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setDriver(ThreadLocal<WebDriver> driver) {
        BaseClass.driver = driver;
    }
}