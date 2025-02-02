package com.framework.utils;

import com.framework.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UselessFileDetector;

public class DriverUtils {
    public static void unWrapDriver(LocalFileDetector localFileDetector) {
            WebDriver unwrappedDriver = BaseTest.getDriver();
            if (ParameterUtil.getBooleanParameter("remote", false)) {

                // Unwrap the driver if it's decorated
                while (!(unwrappedDriver instanceof RemoteWebDriver)
                        && unwrappedDriver instanceof WrapsDriver) {
                    unwrappedDriver = ((WrapsDriver) unwrappedDriver).getWrappedDriver();
                }

                if (unwrappedDriver instanceof RemoteWebDriver) {
                    ((RemoteWebDriver) unwrappedDriver).setFileDetector(localFileDetector);
                }
            }
    }

    public static void unWrapDriver(UselessFileDetector uselessFileDetector) {
        WebDriver unwrappedDriver = BaseTest.getDriver();
        if (ParameterUtil.getBooleanParameter("remote", false)) {

            // Unwrap the driver if it's decorated
            while (!(unwrappedDriver instanceof RemoteWebDriver)
                    && unwrappedDriver instanceof WrapsDriver) {
                unwrappedDriver = ((WrapsDriver) unwrappedDriver).getWrappedDriver();
            }

            if (unwrappedDriver instanceof RemoteWebDriver) {
                ((RemoteWebDriver) unwrappedDriver).setFileDetector(uselessFileDetector);
            }
        }
    }
}
