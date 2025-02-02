package com.framework.utils;

import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlSuite;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParameterUtil {
    private static final ThreadLocal<Map<String, String>> suiteParameters = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, String>> testParameters = new ThreadLocal<>();

    public static void loadParameters(ITestContext context) {
        // Load suite level parameters
        XmlSuite suite = context.getCurrentXmlTest().getSuite();
        Map<String, String> suiteParams = suite.getParameters();
        suiteParameters.set(new HashMap<>(suiteParams));

        // Load test level parameters
        Map<String, String> testParams = context.getCurrentXmlTest().getLocalParameters();
        testParameters.set(new HashMap<>(testParams));

        // Set suite parameters to system properties
        for (Map.Entry<String, String> param : suiteParams.entrySet()) {
            if (System.getProperty(param.getKey()) == null) {
                System.setProperty(param.getKey(), param.getValue());
                LoggerUtil.info("Setting suite parameter: " + param.getKey() + " = " + param.getValue());
            }
        }

        // Set test parameters to system properties (overriding suite parameters if
        // duplicated)
        for (Map.Entry<String, String> param : testParams.entrySet()) {
            System.setProperty(param.getKey(), param.getValue());
            LoggerUtil.info("Setting test parameter: " + param.getKey() + " = " + param.getValue());
        }
    }

    /**
     * Get all parameters (both suite and test level)
     * 
     * @return Map containing all parameters
     */
    public static Map<String, String> getAllParameters() {
        Properties properties = System.getProperties();
        Map<String, String> allParams = new HashMap<>();

        // Add suite parameters
        if (suiteParameters.get() != null) {
            allParams.putAll(suiteParameters.get());
        }

        // Add test parameters (overriding suite parameters if duplicated)
        if (testParameters.get() != null) {
            allParams.putAll(testParameters.get());
        }

        // Add any additional system properties that might have been set
        for (String name : properties.stringPropertyNames()) {
            allParams.put(name, properties.getProperty(name));
        }

        return allParams;
    }

    /**
     * Get only suite level parameters
     * 
     * @return Map containing suite parameters
     */
    public static Map<String, String> getSuiteParameters() {
        return suiteParameters.get() != null ? new HashMap<>(suiteParameters.get()) : new HashMap<>();
    }

    /**
     * Get only test level parameters
     * 
     * @return Map containing test parameters
     */
    public static Map<String, String> getTestParameters() {
        return testParameters.get() != null ? new HashMap<>(testParameters.get()) : new HashMap<>();
    }

    /**
     * Get parameter value by name
     */
    public static String getParameter(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }

    /**
     * Get boolean parameter value
     */
    public static boolean getBooleanParameter(String name, boolean defaultValue) {
        String value = System.getProperty(name);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get integer parameter value
     */
    public static int getIntParameter(String name, int defaultValue) {
        String value = System.getProperty(name);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            LoggerUtil.error("Failed to parse integer parameter: " + name, e);
            return defaultValue;
        }
    }

    /**
     * Clean up thread local variables
     */
    public static void cleanup() {
        suiteParameters.remove();
        testParameters.remove();
    }

    /**
     * Print all parameters for debugging
     */
    public static void printAllParameters() {
        LoggerUtil.info("=== Suite Parameters ===");
        getSuiteParameters().forEach((key, value) -> LoggerUtil.info(String.format("  %s = %s", key, value)));

        LoggerUtil.info("=== Test Parameters ===");
        getTestParameters().forEach((key, value) -> LoggerUtil.info(String.format("  %s = %s", key, value)));

        LoggerUtil.info("=== All System Properties ===");
        getAllParameters().forEach((key, value) -> LoggerUtil.info(String.format("  %s = %s", key, value)));
    }
}