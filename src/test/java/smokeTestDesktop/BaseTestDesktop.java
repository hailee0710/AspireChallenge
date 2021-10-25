package smokeTestDesktop;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import helper.GeneralMethods;
import io.github.bonigarcia.wdm.WebDriverManager;
import pageObject.*;
import ru.yandex.qatools.ashot.Screenshot;

/**
 * BaseTest class is created to be the base of all other test classes.
 * It contains the common variables, setup() function and tearDown() function.
 * All test classes must inherit from this class in order to be able to start and close any session successfully.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Set test run order with Name Ascending order
public class BaseTestDesktop {

    protected static WebDriver driver;
    protected GeneralMethods generalMethods;
    protected BasePage basePage;
    protected LoginPage loginPage;
    protected RegistrationPage registrationPage;
    protected OTPPage otpPage;
    protected BusinessRolePage businessRolePage;
    protected PersonalDetailsPage personalDetailsPage;
    protected BusinessDetailsPage businessDetailsPage;
    protected static GeneralMethods commonMethods = new GeneralMethods();
    final static Logger logger = Logger.getLogger(BaseTestDesktop.class);
    protected final String folderName = "desktop/" + this.getClass().getSimpleName();
    private static boolean initialized = false;

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TestRule testWatcher = new TestWatcher() {

        @Override
        public void starting(Description desc) {
            try {
                cleanImages();
                logger.info("Starting test...");
                setup();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            }

        @Override
        public void finished(Description desc) {
            logger.info("Quitting driver...");
            if (driver != null)
                driver.quit();
        }
        @Override
        public void failed(Throwable e, Description d) {
            logger.debug("Exception is thrown. Creating screenshot...");
            Screenshot debug = generalMethods.takeScreenshot(folderName, null);
            String debugPath = System.getProperty("user.dir") + "/images/" + commonMethods.getProperties("browser") + "/" + folderName + "/debug/" + testName
                    .getMethodName() + "_debug_";
            File debugFolder = new File(System.getProperty("user.dir") + "/images/" + commonMethods.getProperties("browser") + "/" + folderName + "/debug");
            generalMethods.folderCheck(debugFolder);
            debugPath = debugPath + "_1440x892.jpg";
            try {
                ImageIO.write(debug.getImage(), "PNG", new File(debugPath));
                logger.info("Debug image for this error is: " + debugPath);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    };

    /**
     * setup() method is used to initialize webdriver based value of "browser" in Config.properties file.
     * There are 3 types of browser which are supported here:
     * - Chrome
     * - Safari
     * - Selenium Grid
     *
     * @throws MalformedURLException throw exception if URL is not in correct format
     */
    private void setup() throws MalformedURLException {

        //Set up webdriver with the browser specified in config file
        String driverType = commonMethods.getProperties("browser").toLowerCase();

        //Use Switch Case to switch between driver types
        switch (driverType) {
            case "chrome": {
                //WebDriverManager is used to download and setup ChromeDriver
                WebDriverManager.chromedriver().setup();
                //Set custom options for Chrome
                ChromeOptions chromeOptions = new ChromeOptions();
                //Headless mode value is True or False. Value is get from config file
                chromeOptions.setHeadless(Boolean.parseBoolean(commonMethods.getProperties("headlessMode")));
                //Setup window size. Value Width and Height are taken from config file
                //Check for running mode (Head/Headless)
                if (Boolean.parseBoolean(commonMethods.getProperties("headlessMode")))
                    chromeOptions.addArguments("--window-size=" + commonMethods.getProperties("windowWidth") + "," + commonMethods.getProperties("windowHeight"));
                else
                    chromeOptions.addArguments("--window-size=" + (Integer.parseInt(commonMethods.getProperties("windowWidth")) + 16) + "," + (Integer.parseInt(commonMethods.getProperties("windowHeight")) + 132));
                //Disable unexpected notification during test run
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                chromeOptions.addArguments("enable-automation");
                chromeOptions.addArguments("--no-sandbox");
                System.setProperty("webdriver.chrome.silentOutput", "true"); //THIS will surpress all logs expect INFO

                java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
                //Set scrollbar in headless mode to the same with head mode
                chromeOptions.addArguments("--enable-features=OverlayScrollbar");
                chromeOptions.addArguments("--enable-prefer-compositing-to-lcd-text");                //Initialize ChromeDriver
                driver = new ChromeDriver(chromeOptions);
                //Set size for window
                long innerWidth = (long) ((JavascriptExecutor) driver).executeScript("return window.innerWidth");
                long innerHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
                long outerWidth = (long) ((JavascriptExecutor) driver).executeScript("return window.outerWidth");
                long outerHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.outerHeight");
                long borderWidth = outerWidth - innerWidth;
                long borderHeight = outerHeight - innerHeight;
                driver.manage().window().setSize(new Dimension(Integer.parseInt(commonMethods.getProperties("windowWidth")) + Math.toIntExact(borderWidth),
                        (Integer.parseInt(commonMethods.getProperties("windowHeight")) + Math.toIntExact(borderHeight))));
                break;
            }
            case "firefox": {
                //WebDriverManager is used to download and setup ChromeDriver
                WebDriverManager.firefoxdriver().setup();
                //Set custom options for Chrome
                FirefoxOptions ffOptions = new FirefoxOptions();
                //Headless mode value is True or False. Value is get from config file
                ffOptions.setHeadless(Boolean.parseBoolean(commonMethods.getProperties("headlessMode")));
                //Setup window size. Value Width and Height are taken from config file
                //Check for running mode (Head/Headless)
                if (Boolean.parseBoolean(commonMethods.getProperties("headlessMode")))
                    ffOptions.addArguments("--window-size=" + commonMethods.getProperties("windowWidth") + "," + commonMethods.getProperties("windowHeight"));
                else
                    ffOptions.addArguments("--window-size=" + (Integer.parseInt(commonMethods.getProperties("windowWidth")) + 16) + "," + (Integer.parseInt(commonMethods.getProperties("windowHeight")) + 132));
                //Disable unexpected notification during test run
                ffOptions.addArguments("--disable-notifications");
                ffOptions.addArguments("--disable-gpu");
                ffOptions.addArguments("--disable-infobars");
                ffOptions.addArguments("--disable-dev-shm-usage");
                ffOptions.addArguments("--disable-browser-side-navigation");
                ffOptions.addArguments("enable-automation");
                ffOptions.addArguments("--no-sandbox");
                System.setProperty("webdriver.chrome.silentOutput", "true"); //THIS will surpress all logs expect INFO

                java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
                //Set scrollbar in headless mode to the same with head mode
                ffOptions.addArguments("--enable-features=OverlayScrollbar");
                ffOptions.addArguments("--enable-prefer-compositing-to-lcd-text");                //Initialize ChromeDriver
                driver = new FirefoxDriver(ffOptions);
                //Set size for window
                long innerWidth = (long) ((JavascriptExecutor) driver).executeScript("return window.innerWidth");
                long innerHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
                long outerWidth = (long) ((JavascriptExecutor) driver).executeScript("return window.outerWidth");
                long outerHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.outerHeight");
                long borderWidth = outerWidth - innerWidth;
                long borderHeight = outerHeight - innerHeight;
                driver.manage().window().setSize(new Dimension(Integer.parseInt(commonMethods.getProperties("windowWidth")) + Math.toIntExact(borderWidth),
                        (Integer.parseInt(commonMethods.getProperties("windowHeight")) + Math.toIntExact(borderHeight))));
                break;
            }
        }

        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(Long.parseLong(commonMethods.getProperties("implicitlyWait")), TimeUnit.SECONDS);
        //Set Page Load time out
        driver.manage().timeouts().pageLoadTimeout(Long.parseLong(commonMethods.getProperties("pageLoadTimeout")), TimeUnit.SECONDS);

        //Initiate generalMethods and PO
        generalMethods = new GeneralMethods(driver);
        basePage = new BasePage(driver);
        loginPage = new LoginPage(driver);
        registrationPage = new RegistrationPage(driver);
        otpPage = new OTPPage(driver);
        businessRolePage = new BusinessRolePage(driver);
        personalDetailsPage = new PersonalDetailsPage(driver);
        businessDetailsPage = new BusinessDetailsPage(driver);
    }

    private void cleanUpImages() {
        File newImageFolder = new File(System.getProperty("user.dir") + "/images/"
                + commonMethods.getProperties("browser") + "/" + folderName + "/new");
        File diffImageFolder = new File(System.getProperty("user.dir") + "/images/"
                + commonMethods.getProperties("browser") + "/" + folderName + "/diff");
        if (newImageFolder.isDirectory() && newImageFolder.exists()) {
            if (Objects.requireNonNull(newImageFolder.list()).length != 0) {
                logger.info("Cleaning New images folder...");
                try {
                    FileUtils.cleanDirectory(newImageFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logger.info("Done");
            }
        }
        if (diffImageFolder.isDirectory() && diffImageFolder.exists()) {
            if (Objects.requireNonNull(diffImageFolder.list()).length != 0) {
                logger.info("Cleaning Diff images folder...");
                try {
                    FileUtils.cleanDirectory(diffImageFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logger.info("Done");
            }
        }
    }
    
    /**
     * cleanRefImages() method is used to clean all reference images based value of
     * "cleanRefImages" in Config.properties file.
     */
    private void cleanImages() {
        if (!initialized) {

            boolean cleanRefImages = Boolean.parseBoolean(commonMethods.getProperties("cleanImages"));
            try {
                File imageFolder = new File(System.getProperty("user.dir") + "/images/"
                        + commonMethods.getProperties("browser") + "/" + folderName);
                if (cleanRefImages) {
                    System.out.print("Option to clean all reference images is set to TRUE. Deleting all images in "
                            + imageFolder);
                    FileUtils.cleanDirectory(imageFolder);
                    logger.info("----> Done.");
                } else
                    logger.info("Option to clean all reference images is set to FALSE. Continue to test run.");
            } catch (IOException e) {
                logger.info("This is the first run.");
            } catch (IllegalArgumentException i) {
                logger.info("This is the first run");
            }
            cleanUpImages();
            initialized = true;
        }
    }
}
