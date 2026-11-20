package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ScreenshotOnFailureExtension;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseTest {

    // показываем только серьёзные ошибки Selenium, предупреждения скрываем
    private static final Logger SELENIUM_LOGGER = Logger.getLogger("org.openqa.selenium");

    static {
        SELENIUM_LOGGER.setLevel(Level.SEVERE);
    }

    protected WebDriver driver;

    @BeforeEach
    void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        // отключаем всплывающее окно Chrome «смените пароль» после входа
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
        ));

        // Запуск без окна браузера: HEADLESS=1 mvn test. Так тесты идут в CI (GitHub Actions), где экрана нет;
        // драйвер Chrome нужной версии скачивает Selenium Manager — хранить chromedriver в проекте не нужно
        boolean headless = "1".equals(System.getenv("HEADLESS"));
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        if (!headless) {
            driver.manage().window().maximize();
        }
    }

    @AfterEach
    void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}