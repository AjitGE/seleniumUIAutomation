package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class AlertsPage extends BasePage {
    private final By jsAlertButton = By.cssSelector("button[onclick='jsAlert()']");
    private final By resultText= By.id( "result");

    public AlertsPage(WebDriver driver) {
        super(driver);
    }

    public void triggerAlert() {
        waitUtil.waitForElementClickable(jsAlertButton).click();
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public String getResult() {
        return waitUtil.waitForElementVisible(resultText).getText();
    }
}