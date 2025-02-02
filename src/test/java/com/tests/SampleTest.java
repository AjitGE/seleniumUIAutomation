package src.test.java.com.tests;

import com.framework.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class SampleTest extends BaseTest {

    @Test(priority = 0, description = "Open Google Page")
    public void openPage() {
        getDriver().get("https://www.google.com");
        Assert.assertTrue(Objects.requireNonNull(getDriver().getTitle()).contains("Google"));
    }

    @Test(dependsOnMethods = "openPage", description = "Search Operation")
    public void performSearch() {
        WebElement searchBox = getDriver().findElement(By.xpath("//textarea[@title='Search']"));
        searchBox.click();
        searchBox.sendKeys("Selenium Automation");
    }
}