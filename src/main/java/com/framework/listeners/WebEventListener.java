package com.framework.listeners;

import com.framework.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class WebEventListener implements WebDriverListener {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // List of common HTML attributes to check
    private static final List<String> COMMON_ATTRIBUTES = Arrays.asList(
            "id", "class", "name", "value", "type", "href", "src", "alt",
            "title", "role", "aria-label", "aria-describedby", "placeholder",
            "disabled", "readonly", "required", "checked", "selected",
            "maxlength", "minlength", "pattern", "target", "rel", "style",
            "data-testid", "data-cy", "data-automation-id");

    private void log(String message) {
        LoggerUtil.info(message);
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        log("Navigating to URL: " + url);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        log("Navigated to URL: " + url);
        log("Page Title: " + driver.getTitle());
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log("Trying to find element: " + locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        log("Found element: " + getElementDetails(result));
    }

    @Override
    public void beforeClick(WebElement element) {
        log("Clicking on element: " + getElementDetails(element));
    }

    @Override
    public void afterClick(WebElement element) {
        log("Clicked on element successfully");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log("Sending keys to element: " + getElementDetails(element));
        log("Keys to send: " + String.join("", keysToSend));
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        log("Sent keys successfully");
    }

    @Override
    public void beforeClear(WebElement element) {
        log("Clearing element: " + getElementDetails(element));
    }

    @Override
    public void afterClear(WebElement element) {
        log("Cleared element successfully");
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        String errorMessage = String.format("Error occurred during %s: %s",
                method.getName(),
                e.getTargetException().getMessage());
        log("❌ " + errorMessage);
        e.getTargetException().printStackTrace();
    }

    private String getElementDetails(WebElement element) {
        try {
            StringBuilder details = new StringBuilder();
            details.append("\n  Element Details:");
            details.append("\n  └── Locator: ").append(element);
            details.append("\n    └── Tag: ").append(element.getTagName());

            // Get text content if any
            String text = element.getText().trim();
            if (!text.isEmpty()) {
                if (text.length() > 50) {
                    text = text.substring(0, 47) + "...";
                }
                details.append("\n    └── Text: ").append(text);
            }

            // Check if element is displayed, enabled, and selected
            details.append("\n    └── State:");
            details.append("\n        ├── Displayed: ").append(element.isDisplayed());
            details.append("\n        ├── Enabled: ").append(element.isEnabled());
            details.append("\n        └── Selected: ").append(element.isSelected());

            // Get element location and size
            details.append("\n    └── Position:");
            details.append("\n        ├── Location: ").append(element.getLocation());
            details.append("\n        └── Size: ").append(element.getSize());

            // Check all common attributes
            boolean hasAttributes = false;
            StringBuilder attrDetails = new StringBuilder("\n    └── Attributes:");

            for (String attr : COMMON_ATTRIBUTES) {
                String domValue = element.getDomAttribute(attr);
                String propValue = element.getDomProperty(attr);

                if (domValue != null && !domValue.isEmpty()) {
                    hasAttributes = true;
                    attrDetails.append("\n        ├── ").append(attr).append(": ").append(domValue);
                }
                // Check if property value is different from attribute value
                if (propValue != null && !propValue.isEmpty() && !propValue.equals(domValue)) {
                    hasAttributes = true;
                    attrDetails.append(" (property: ").append(propValue).append(")");
                }
            }

            // Get any data-* attributes
            String script = "var items = {}; for (var i = 0; i < arguments[0].attributes.length; i++) { " +
                    "var attr = arguments[0].attributes[i]; " +
                    "if (attr.name.startsWith('data-')) { items[attr.name] = attr.value; } }; return items;";

            try {
                if (element instanceof org.openqa.selenium.JavascriptExecutor) {
                    org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) element;
                    Object dataAttrs = js.executeScript(script, element);
                    if (dataAttrs != null) {
                        hasAttributes = true;
                        attrDetails.append("\n        └── Data Attributes: ").append(dataAttrs);
                    }
                }
            } catch (Exception e) {
                // Ignore JavaScript execution errors
            }

            if (hasAttributes) {
                details.append(attrDetails);
            }

            // Get computed CSS properties
            details.append("\n    └── CSS Properties:");
            List<String> cssProps = Arrays.asList("color", "background-color", "font-size", "display", "visibility");
            for (String prop : cssProps) {
                String value = element.getCssValue(prop);
                if (value != null && !value.isEmpty()) {
                    details.append("\n        ├── ").append(prop).append(": ").append(value);
                }
            }

            return details.toString();
        } catch (Exception e) {
            return "Could not get element details: " + e.getMessage();
        }
    }
}