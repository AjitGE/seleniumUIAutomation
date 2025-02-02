package com.framework.base;

import com.framework.listeners.WebEventListener;
import com.framework.utils.ParameterUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected static ThreadLocal<WebDriver> localThread = new ThreadLocal<>();
    private static final WebEventListener eventListener = new WebEventListener();
    private  static String GRID_URL ;
    private  static boolean IS_REMOTE ;


    public static WebDriver getDriver() {
        return localThread.get();
    }


    public static void setup() {
        WebDriver driver;
        IS_REMOTE = ParameterUtil.getBooleanParameter("remote",false);
        String BROWSER = ParameterUtil.getParameter("Browser", "chrome");
        GRID_URL = ParameterUtil.getParameter("gridUrl","http://localhost:4444/");
        try {
            if (IS_REMOTE) {
                driver = createRemoteDriver(BROWSER);
            } else {
                driver = createLocalDriver(BROWSER);
            }

            // Set implicit wait and page load timeout before decoration
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

            // Maximize window before decoration
            driver.manage().window().maximize();

            // Wrap the driver with event firing decorator
            WebDriver decoratedDriver = new EventFiringDecorator<>(eventListener).decorate(driver);
            localThread.set(decoratedDriver);



        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        }
    }

    private static WebDriver createLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(createChromeOptions());
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(createEdgeOptions());
            default:
                throw new IllegalArgumentException("Browser " + browser + " is not supported");
        }
    }

    private static RemoteWebDriver createRemoteDriver(String browser) throws Exception {
        switch (browser.toLowerCase()) {
            case "chrome":
                return new RemoteWebDriver(new URL(GRID_URL), createChromeOptions());
            case "edge":
                return new RemoteWebDriver(new URL(GRID_URL), createEdgeOptions());
            default:
                throw new IllegalArgumentException("Browser " + browser + " is not supported for remote execution");
        }
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (IS_REMOTE) {

            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }
        return options;
    }

    private static EdgeOptions createEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (IS_REMOTE) {
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }
        return options;
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                // Log but don't throw
                System.err.println("Error during driver quit: " + e.getMessage());
            } finally {
                localThread.remove();
            }
        }
    }
}