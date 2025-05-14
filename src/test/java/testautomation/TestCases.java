package testautomation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {
    WebDriver driver;

    @BeforeEach
    public void setup() {

        WebDriverManager.chromedriver().setup(); // Automatically handles ChromeDriver setup
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200"); // Headless mode
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {

        // if (driver != null) {
        //     driver.quit();
        // }
    }

    @Test
    public void testValidLogin() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        // Validate successful login
        WebElement message = driver.findElement(By.className("post-title"));
        assertTrue(message.getText().contains("Logged In Successfully"));
    }
    @Test
    public void testInvalidLoginWrongPassword() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("WrongPassword");
        driver.findElement(By.id("submit")).click();

        // Validate error message
        WebElement error = driver.findElement(By.id("error"));
        assertTrue(error.getText().contains("Your password is invalid!"));
    }
    @Test
    public void testEmptyFormSubmission() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("submit")).click();

        WebElement error = driver.findElement(By.id("error"));
        assertTrue(error.isDisplayed());

    }
    @Test
    public void testLoginButtonVisibility() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement loginBtn = driver.findElement(By.id("submit"));
        assertTrue(loginBtn.isDisplayed());

    }
    @Test
    public void testUsernameFieldMaxLength() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        String longUsername = "a".repeat(256); // sending 256 characters
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys(longUsername);

        assertEquals(256, usernameField.getAttribute("value").length());
    }
    @Test
    public void testLoginButtonClickability() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement loginBtn = driver.findElement(By.id("submit"));

        assertTrue(loginBtn.isDisplayed());
        assertTrue(loginBtn.isEnabled());

        // Enter valid credentials to test button functionality
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        loginBtn.click();

        assertTrue(driver.getCurrentUrl().contains("logged-in-successfully-and button is clic"));
    }
    @Test
    public void testPostLoginRedirection() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        // Confirm redirect to success page
        assertTrue(driver.getCurrentUrl().contains("logged-in-successfully"));
    }
    @Test
    public void testMultipleFailedLoginAttempts() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        for (int i = 0; i < 3; i++) {
            driver.findElement(By.id("username")).clear();
            driver.findElement(By.id("password")).clear();

            driver.findElement(By.id("username")).sendKeys("student");
            driver.findElement(By.id("password")).sendKeys("WrongPass" + i);
            driver.findElement(By.id("submit")).click();

            WebElement error = driver.findElement(By.id("error"));
            assertTrue(error.isDisplayed());
            assertEquals("Your password is invalid!", error.getText());
        }
    }

}