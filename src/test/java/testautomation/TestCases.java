package testautomation;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCases {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testValidLogin() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("post-title")));
        assertTrue(message.getText().trim().contains("Logged In Successfully"));
    }

    @Test
    public void testInvalidLoginWrongPassword() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("WrongPassword");
        driver.findElement(By.id("submit")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertEquals("Your password is invalid!", error.getText().trim());
    }

    @Test
    public void testEmptyFormSubmission() {
        driver.get("https://practicetestautomation.com/practice-test-login/");
        driver.findElement(By.id("submit")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        assertTrue(error.isDisplayed());
    }

    @Test
    public void testLoginButtonVisibility() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
        assertTrue(loginBtn.isDisplayed());
        assertTrue(loginBtn.isEnabled());
    }

    @Test
    public void testUsernameFieldMaxLength() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        String longUsername = "a".repeat(256);
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys(longUsername);

        assertEquals(256, usernameField.getAttribute("value").length());
    }

    @Test
    public void testLoginButtonClickability() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
        assertTrue(loginBtn.isDisplayed());
        assertTrue(loginBtn.isEnabled());

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        loginBtn.click();

        assertTrue(wait.until(ExpectedConditions.urlContains("logged-in-successfully")));
    }

    @Test
    public void testPostLoginRedirection() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        assertTrue(wait.until(ExpectedConditions.urlContains("logged-in-successfully")));
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

            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
            assertTrue(error.isDisplayed());
            assertEquals("Your password is invalid!", error.getText().trim());
        }
    }
}
