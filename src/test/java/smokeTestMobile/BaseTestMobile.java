package smokeTestMobile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Verifier;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import helper.GeneralMethods;
import io.github.bonigarcia.wdm.WebDriverManager;
import pageObject.BasePage;
import ru.yandex.qatools.ashot.Screenshot;

/**
 * BaseTest class is created to be the base of all other test classes.
 * It contains the common variables, setup() function and tearDown() function.
 * All test classes must inherit from this class in order to be able to start and close any session successfully.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Set test run order with Name Ascending order
public class BaseTestMobile {

    protected static GeneralMethods commonMethods = new GeneralMethods();
    protected static WebDriver driver;
    protected static BasePage basePage;
    protected String folderName = "android/" + this.getClass().getSimpleName();
    protected GeneralMethods generalMethods;
    final static Logger logger = Logger.getLogger(smokeTestMobile.BaseTestMobile.class);
    private static boolean initialized = false;
    private static int errorNo = 0;
    protected ArrayList<String> errorList = new ArrayList<>();

    @Rule
    public TestName testName = new TestName();

    @Rule
    public Verifier verifier = new Verifier() {
        @Override
        public void verify() {
            Assert.assertEquals("Errors happened during test run. The following steps have error: " + errorList, 0,
                    errorList.size());
            generalMethods.printSuccessMsg("Result of " + testName.getMethodName() + " is: PASSED");
        }
    };

    @Rule
    public TestRule testWatcher = new TestWatcher() {

        @Override
        public void starting(Description desc) {
            try {
                cleanImages();
                logger.info("Starting test...");
                setup();
                errorList.clear();
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
            String error = e.getMessage();
            if (!error.contains("Errors happened during test run.")) {
                logger.debug("Exception is thrown. Creating screenshot...");
                errorNo++;
                Screenshot debug = generalMethods.takeScreenshot(folderName, null);
                String debugPath = System.getProperty("user.dir") + "/images/" + commonMethods.getProperties("browser") + "/" + folderName + "/debug/" + testName.getMethodName() +"_debug_";
                File debugFolder = new File(System.getProperty("user.dir") + "/images/" + commonMethods.getProperties("browser") + "/" + folderName + "/debug");
                generalMethods.folderCheck(debugFolder);
                if (folderName.contains("desktop")) {
                    debugPath = debugPath + errorNo + "_1440x892.jpg";
                } else if (folderName.contains("android")) {
                    debugPath = debugPath + errorNo + "_360x640.jpg";
                } else if (folderName.contains("iPhone")) {
                    debugPath = debugPath + errorNo + "_375x667.jpg";
                }
                try {
                    ImageIO.write(debug.getImage(), "PNG", new File(debugPath));
                    logger.info("Debug image for this error is: " + debugPath);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                // Path path = Paths.get(debugPath + ".html");
                // String html = driver.getPageSource();
                // byte[] strToBytes = html.getBytes();
                // try {
                //     Files.write(path, strToBytes);
                // } catch (IOException e1) {
                //     // TODO Auto-generated catch block
                //     e1.printStackTrace();
                // }
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
                Map<String, Object> deviceMetrics = new HashMap<>();
                deviceMetrics.put("width", 360);
                deviceMetrics.put("height", 640);
                deviceMetrics.put("pixelRatio", 3.0);
                Map<String, Object> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceMetrics", deviceMetrics);
                mobileEmulation.put("userAgent",
                        "Mozilla/5.0 (Linux; Android 5.0;" +
                                "SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) " +
                                "Chrome/90.0.4430.212 Mobile Safari/537.36 qa-testing"
                );
                //Set custom options for Chrome
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                //Headless mode value is True or False. Value is get from config file
                chromeOptions.setHeadless(Boolean.parseBoolean(commonMethods.getProperties("headlessMode")));
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-browser-side-navigation");
                chromeOptions.addArguments("enable-automation");
                System.setProperty("webdriver.chrome.silentOutput", "true"); //THIS will surpress all logs expect INFO
                java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
                //Initialize ChromeDriver
                driver = new ChromeDriver(chromeOptions);
                //Set Location for browser
                ((JavascriptExecutor) driver).executeScript("window.navigator.geolocation.getCurrentPosition=function(success){" +
                        "var position = {\"coords\" : {\"latitude\": \"59.334591\",\"longitude\": \"18.063240\"}};" +
                        "success(position);}");
                break;
            }
            case "safari": {
                //Set custom options for Safari
                SafariOptions safariOptions = new SafariOptions();
                //setUseTechnologyPreview value is True or False. If true, the Safari Technology Preview version will be used.
                safariOptions.setUseTechnologyPreview(Boolean.parseBoolean(commonMethods.getProperties("setUseTechnologyPreview")));
                //Initialize Safari driver
                driver = new SafariDriver(safariOptions);
                //Since there is no custom command to run Safari in specific window size, maximize the window.
                driver.manage().window().maximize();
                break;
            }
            case "grid": {
                //WebDriverManager is used to download and setup ChromeDriver
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability("browserName", "chrome");
                chromeOptions.setCapability("platformName", Platform.ANY);
                //Headless mode value is True or False. Value is get from config file
                chromeOptions.setHeadless(Boolean.parseBoolean(commonMethods.getProperties("headlessMode")));
                //Setup window size. Value Width and Height are taken from config file
                chromeOptions.addArguments("--window-size=" + commonMethods.getProperties("windowWidth") + "," + commonMethods.getProperties("windowHeight"));
                //Disable unexpected notification during test run
                chromeOptions.addArguments("--disable-notifications");
                //Set custom UA
                chromeOptions.addArguments("--user-agent=" + commonMethods.getProperties("userAgent"));
                driver = new RemoteWebDriver(new URL(commonMethods.getProperties("NodeURL")), chromeOptions);
                break;
            }
        }

        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(Long.parseLong(commonMethods.getProperties("implicitlyWait")), TimeUnit.SECONDS);
        //Set Page Load time out
        driver.manage().timeouts().pageLoadTimeout(Long.parseLong(commonMethods.getProperties("pageLoadTimeout")), TimeUnit.SECONDS);

        generalMethods = new GeneralMethods(driver);

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

    private void cleanUpImages() {
        File newImageFolder = new File(System.getProperty("user.dir") + "/images/" + commonMethods.getProperties("browser") + "/" + folderName + "/new");
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

//    @After
//    public void tearDown() {
//        //Close session
//        driver.quit();
//    }
}
