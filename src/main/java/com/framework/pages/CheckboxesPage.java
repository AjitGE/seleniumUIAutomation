package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class CheckboxesPage extends BasePage {
    private final By checkbox1= By.cssSelector( "input[type='checkbox']:nth-of-type(1)");
    private final By checkbox2= By.cssSelector( "input[type='checkbox']:nth-of-type(2)");

    public CheckboxesPage(WebDriver driver) {
        super(driver);
    }

    public void toggleCheckbox1() {
        waitUtil.waitForElementVisible(checkbox1).click();
    }

    public void toggleCheckbox2() {
        waitUtil.waitForElementVisible(checkbox2).click();
    }


    public boolean isCheckbox1Selected() {
        return waitUtil.waitForElementPresent(checkbox1).isSelected();
    }

    public boolean isCheckbox2Selected() {
        return waitUtil.waitForElementPresent(checkbox2).isSelected();
    }
}