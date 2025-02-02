package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class KeyPressesPage extends BasePage {
    private final By inputField = By.id("target");
    private final By resultText = By.id("result");

    public KeyPressesPage(WebDriver driver) {
        super(driver);
    }

    public void pressKey(Keys key) {
        waitUtil.waitForElementPresent(inputField).sendKeys(key);
    }

    public String getResult() {
        return waitUtil.waitForElementVisible(resultText).getText();
    }
}