# Selenium Test Automation Framework

A robust test automation framework built with Selenium WebDriver, TestNG, and Docker Selenium Grid, optimized for Apple Silicon (ARM64).

## 🚀 Features

- Page Object Model (POM) design pattern
- Docker Selenium Grid with ARM64 support
- Parallel test execution with thread safety
- Comprehensive reporting with Extent Reports
- Automated screenshot capture on test failures
- Full test execution video recording
- Smart retry mechanism for flaky tests
- Cross-browser testing (Chrome, Firefox, Safari\*)
- Enhanced wait utilities with custom conditions
- Structured logging with Log4j2
- Data-driven testing support (Excel, CSV, JSON)
- Environment-specific configuration
- API testing integration
- Custom annotations for test management
- CI/CD ready configuration

## 🛠️ Technology Stack

- Java 11
- Selenium WebDriver 4.28.1
- TestNG 7.10.2
- Extent Reports 5.1.2
- Log4j2
- Docker & Docker Compose
- Maven
- REST Assured (API Testing)
- Apache POI (Excel Operations)
- Jackson (JSON Operations)

## 🔧 Setup Instructions

### Prerequisites

- Java 11 or higher
- Maven 3.8+
- Docker Desktop for Mac (Apple Silicon)

### Configuration

1. Environment Configuration (`config.properties`):
   properties
   browser=chrome
   baseUrl=https://www.example.com
   implicit.wait=10
   explicit.wait=20
   retry.count=2
   screenshot.policy=FAILURE_ONLY
   extent.report.theme=DARK

2. TestNG Configuration (`testng.xml`):
   xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
   <suite name="Regression Suite" parallel="tests" thread-count="3">
   <listeners>
   <listener class-name="com.framework.listeners.TestListener"></listener>
   <listener class-name="com.framework.retry.RetryAnnotationTransformer"></listener>
   </listeners>
   <test name="Chrome Tests">
   <parameter name="browser" value="chrome"></parameter>
   <classes>
   <class name="com.tests.ElementInteractionTest"></class>
   </classes>
   </test>
   </suite>

### Starting Selenium Grid

1. Start Docker containers:
   bash:README.md
   docker-compose up -d

2. Verify Grid is running:
   - Grid Console: http://localhost:4444/ui
   - Chrome Node: http://localhost:6900 (VNC password: secret)
   - Firefox Node: http://localhost:6902 (VNC password: secret)

## 🏃 Running Tests

1. Run all tests:
   bash:README.md
   mvn clean test

2. Run specific test suite:
   bash:README.md
   mvn clean test -DsuiteXmlFile=testng.xml

3. Run with specific browser:
   bash:README.md
   mvn clean test -Dbrowser=chrome
4. Run tests in parallel:
   bash:README.md
   mvn clean test -Dthreads=3

## 📁 Project Structure

selenium-framework/
├── src/
│ ├── main/
│ │ └── java/
│ │ └── com/framework/
│ │ ├── base/
│ │ │ ├── BasePage.java
│ │ │ └── BaseTest.java
│ │ ├── config/
│ │ │ ├── Configuration.java
│ │ │ └── DriverFactory.java
│ │ ├── pages/
│ │ │ ├── LoginPage.java
│ │ │ └── HomePage.java
│ │ ├── utils/
│ │ │ ├── DriverUtils.java
│ │ │ ├── WaitUtil.java
│ │ │ ├── ScreenshotUtil.java
│ │ │ ├── ExcelUtil.java
│ │ │ └── APIUtil.java
│ │ └── listeners/
│ │ ├── TestListener.java
│ │ └── RetryAnalyzer.java
│ └── test/
│ └── java/
│ └── com/tests/
│ ├── LoginTest.java
│ └── HomePageTest.java
├── resources/
│ ├── config.properties
│ ├── log4j2.xml
│ └── testdata/
│ ├── testdata.xlsx
│ └── api-payloads/
├── docker-compose.yaml
├── pom.xml
├── testng.xml
└── README.md

## 📝 Code Examples

### Page Object Example

java
public class LoginPage extends BasePage {
@FindBy(id = "username")
private WebElement usernameInput;
@FindBy(id = "password")
private WebElement passwordInput;
@FindBy(id = "login-btn")
private WebElement loginButton;
public LoginPage(WebDriver driver) {
super(driver);
PageFactory.initElements(driver, this);
}
public HomePage login(String username, String password) {
waitUtil.waitForElementVisible(usernameInput);
usernameInput.sendKeys(username);
passwordInput.sendKeys(password);
loginButton.click();
return new HomePage(driver);
}
}

### Test Example

java
@Test(dataProvider = "loginData")
public void testLogin(String username, String password, String expectedTitle) {
LoginPage loginPage = new LoginPage(driver);
HomePage homePage = loginPage.login(username, password);
Assert.assertEquals(homePage.getTitle(), expectedTitle);
}

## 📊 Test Reports

- HTML Reports: `test-output/ExtentReport/TestReport.html`
- Screenshots: `test-output/screenshots/[date]/[timestamp]/`
- Videos: `test-output/videos/`
- Logs: `logs/automation.log`

### Sample Report Structure

Test Report
├── Dashboard
│ ├── Test Summary
│ └── Category Summary
├── Tests
│ ├── Login Tests
│ │ ├── Valid Login Test
│ │ └── Invalid Login Test
│ └── Home Page Tests
└── Media
├── Screenshots
└── Videos

## 🐳 Docker Configuration

yaml
version: '3'
services:
selenium-hub:
image: seleniarm/hub:4.16.1
container_name: selenium-hub
ports:
"4444:4444"
"4443:4443"
chrome:
image: seleniarm/node-chromium:4.16.1
shm_size: 2gb
depends_on:
selenium-hub
environment:
SE_EVENT_BUS_HOST=selenium-hub
SE_EVENT_BUS_PUBLISH_PORT=4442
SE_EVENT_BUS_SUBSCRIBE_PORT=4443
ports:
"6900:5900"
firefox:
image: seleniarm/node-firefox:4.16.1
shm_size: 2gb
depends_on:
selenium-hub
environment:
SE_EVENT_BUS_HOST=selenium-hub
SE_EVENT_BUS_PUBLISH_PORT=4442
SE_EVENT_BUS_SUBSCRIBE_PORT=4443
ports:
"6902:5900"

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
