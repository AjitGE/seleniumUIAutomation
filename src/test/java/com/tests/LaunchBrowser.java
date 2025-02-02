package com.tests;

import com.framework.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class LaunchBrowser extends BaseTest {
    protected WebDriver driver;


    @BeforeTest(alwaysRun = true)
    public void initializeTest() {
        setup();
        driver = getDriver();
    }

    @AfterTest(alwaysRun = true)
    public void cleanupTest() {
        driver = null;
    }
}
