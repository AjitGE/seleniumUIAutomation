package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class LoginPage extends BasePage {
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By successMessage = By.cssSelector(".flash.success");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        waitUtil.waitForElementPresent(usernameInput).sendKeys(username);
        waitUtil.waitForElementPresent(passwordInput).sendKeys(password);
        waitUtil.retryingClick(loginButton);
    }

    public String getSuccessMessage() {
        return waitUtil.waitForElementVisible(successMessage).getText();
    }
}