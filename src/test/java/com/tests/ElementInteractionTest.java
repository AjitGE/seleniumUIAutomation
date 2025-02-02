package com.tests;

import com.framework.pages.*;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class ElementInteractionTest extends LaunchBrowser {
        private HomePage homePage;

        @BeforeMethod(alwaysRun = true)
        public void setupTest() {
                driver.get("https://the-internet.herokuapp.com/");
                homePage = new HomePage(driver);
        }

        @Test(priority = 1, description = "Test Dynamic Loading", groups = {
                        "regression" })
        public void testDynamicLoading() {
                DynamicLoadingPage dynamicPage = homePage.navigateToDynamicLoading();
                dynamicPage.clickExample2();
                dynamicPage.clickStart();
                Assert.assertEquals(dynamicPage.getFinishText(), "Hello World!");
        }

        @Test(priority = 2, description = "Test Drag and Drop", groups = {
                        "regression" })
        public void testDragAndDrop() {
                DragAndDropPage dragAndDropPage = homePage.navigateToDragAndDrop();
                dragAndDropPage.dragAToB();
                Assert.assertEquals(dragAndDropPage.getColumnAText(), "B");
        }

        @Test(priority = 3, description = "Test Form Authentication", groups = {
                        "input", "regression" })
        public void testFormAuthentication() {
                LoginPage loginPage = homePage.navigateToLogin();
                loginPage.login("tomsmith", "SuperSecretPassword!");
                Assert.assertTrue(loginPage.getSuccessMessage()
                                .contains("You logged into a secure area!"));
        }

        @Test(priority = 4, description = "Test Checkboxes", groups = { "input",
                        "regression" })
        public void testCheckboxes() {
                CheckboxesPage checkboxesPage = homePage.navigateToCheckboxes();
                if (!checkboxesPage.isCheckbox1Selected()) {
                        checkboxesPage.toggleCheckbox1();
                }
                if (checkboxesPage.isCheckbox2Selected()) {
                        checkboxesPage.toggleCheckbox2();
                }
                Assert.assertTrue(checkboxesPage.isCheckbox1Selected());
                Assert.assertFalse(checkboxesPage.isCheckbox2Selected());
        }

        @Test(priority = 5, description = "Test Key Presses", groups = { "input",
                        "regression" })
        public void testKeyPresses() {
                KeyPressesPage keyPressesPage = homePage.navigateToKeyPresses();
                keyPressesPage.pressKey(Keys.SPACE);
                Assert.assertTrue(keyPressesPage.getResult().contains("SPACE"));
        }

        @Test(priority = 6, description = "Test JavaScript Alerts", groups = {
                        "regression" })
        public void testJavaScriptAlerts() {
                AlertsPage alertsPage = homePage.navigateToAlerts();
                alertsPage.triggerAlert();
                alertsPage.acceptAlert();
                Assert.assertEquals(alertsPage.getResult(), "You successfully clicked an alert");
        }

        @Test(priority = 7, description = "Test File Upload", groups = { "input", "regression" })
        public void testFileUpload() {
                FileUploadPage fileUploadPage = homePage.navigateToFileUpload();
                String filePath = System.getProperty("user.dir") + "/src/main/resources/testData/sample.txt";
                fileUploadPage.uploadFile(filePath);
                fileUploadPage.submitUpload();
                Assert.assertTrue(fileUploadPage.getUploadedFileName().contains("sample.txt"));
        }

        @Test(priority = 8, groups = { "regression" })
        public void methodCleanup() {

                try {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        if (js.executeScript("return window.jQuery") != null) {
                                Boolean ajaxComplete = (Boolean) js.executeScript("return jQuery.active == 0");
                                Assert.assertTrue(ajaxComplete, "Ajax requests are still active");
                        }
                } catch (Exception e) {
                        System.out.println("jQuery is not available on this page");
                }
        }
}