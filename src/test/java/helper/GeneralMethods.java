package helper;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.yandex.qatools.ashot.shooting.ShootingStrategies.scaling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.CaptureElement;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * This Class contains all methods that can be used across all the sites
 */
public class GeneralMethods {
    private static WebDriver driver;

    public GeneralMethods(WebDriver driver) {
        GeneralMethods.driver = driver;
    }

    public GeneralMethods() {}

    final static Logger logger = Logger.getLogger(GeneralMethods.class);

    //region Read Data methods

    /**
     * getProperties() method is used to read data from Config.properties file.
     *
     * @param key is the keyword defined in Config file.
     * @return value associated to the keyword provided.
     */
    private String loadProperties(String filePath, String key) {
        String value = "";
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            value = prop.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * getProperties() method is used to read data from Config.properties file.
     *
     * @param key is the keyword defined in Config file.
     * @return value associated to the keyword provided.
     */
    public String getProperties(String key) {
        //Try to read Config.properties file
        String path = System.getProperty("user.dir") + "/Config.properties";
        return loadProperties(path, key);
    }

    /**
     * getTestData() is method to get data from [Brand]TestData.properties file.
     *
     * @param key   is the keyword defined in Config file.
     * @return value associated to the keyword provided.
     */
    public String getTestData(String key) {
        String path = System.getProperty("user.dir") + "/src/test/resources/TestData.properties";
        return loadProperties(path, key);
    }

    public void clickWithJavaExecuter(WebElement element){
        logger.info("Clicking on  " + element);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
        logger.info("Clicked");
    }

    //endregion

    //region Wait methods

    public void waitForImagesToLoad() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
// Getting DOM status
        Object result = jse.executeScript("return document.readyState;");
        System.out.println("=> The status is : " + result.toString());
// Checking DOM loading is completed or not?
        if (result.equals("complete")) {
            // Fetching images count
            result = jse.executeScript("return document.images.length");
            int imagesCount = Integer.parseInt(result.toString());
            boolean allLoaded = false;
            // Checking and waiting until all the images are getting loaded
            while (!allLoaded) {
                int count = 0;
                for (int i = 0; i < imagesCount; i++) {
                    result = jse.executeScript("return document.images[" + i + "].complete && typeof document.images[" + i + "].naturalWidth != \"undefined\" && document.images[" + i + "].naturalWidth > 0");
                    boolean loaded = (Boolean) result;
                    if (loaded) count++;
                }
                // Breaking the while loop if all the images loading completes
                if (count == imagesCount) {
                    System.out.println("=> All the Images are loaded...");
                    break;
                } else {
                    System.out.println("=> Not yet loaded...");
                }
                pause(1000);
            }
        }
    }

    /**
     * waitInSeconds() is the method to pause the thread for a specified amount of time
     *
     * @param millisec used to define amount of time the you need the thread to sleep. Time Unit: Millisecond.
     */
    public void pause(long millisec) {
        try {
            logger.info("Pausing for " + millisec + " milliseconds");
            MILLISECONDS.sleep(millisec);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * waitForElementToBeVisible() method is used to wait for an element to be visible within time out.
     *
     * @param element is the element that need to be visible
     */
    public void waitForElementToBeVisible(WebElement element, int timeout) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout, SECONDS)
                .pollingEvery(100, MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        logger.info("Waiting for  " + element + " to be visible");
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * waitForElementToBeHidden() method is used to wait for an element to be hidden within time out.
     *
     * @param element is the element that need to be hidden
     */
    public void waitForElementToBeHidden(WebElement element) {
        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            logger.info("Waiting for  " + element + " to be hidden");
            int i = 0;
            while (element.isDisplayed() && i < 300) {
                MILLISECONDS.sleep(100);
                i++;
            }
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            logger.info("Element is already hidden");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(Long.parseLong(getProperties("implicitlyWait")), TimeUnit.SECONDS);
    }

    public void waitForElementToNotExist(By element, int timeout) {
        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        try {
            logger.info("Waiting for  " + element + " to be removed");
            int i = 0;
            while (driver.findElements(element).size() > 0 && i < 500) {
                MILLISECONDS.sleep(100);
                i++;
            }
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
            logger.info("Element is already hidden");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //Set Implicitly wait time
        driver.manage().timeouts().implicitlyWait(Long.parseLong(getProperties("implicitlyWait")), TimeUnit.SECONDS);
    }

    /**
     * waitForElementToBeClickable() method is used to wait for an element to be clickable within time out.
     *
     * @param element is the element that need to be clickable
     */
    public void waitForElementToBeClickable(WebElement element) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, SECONDS)
                .pollingEvery(100, MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        logger.info("Waiting for  " + element + " to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //endregions

    //region Common methods

    public Screenshot takeScreenshot(String device, @Nullable By elementToBeTaken) {
        List<WebElement> images = driver.findElements(By.xpath("//div[@id='main']//img"));
        if (!images.isEmpty()) {
            for (WebElement image : images) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", image);
            }
        }
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0)");
        //Create a new Screenshot object to save the new image.
        Screenshot screenshot = null;
        //Check if an element or the entire page will be taken.
        if (elementToBeTaken == null) {
            logger.info("Taking screenshot of the entire page to compare...");
            //Using 'ashot' lib to take screenshot of the entire page
            if (device.contains("desktop"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
            else if (device.contains("android"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(scaling(3), 100)).takeScreenshot(driver);
            else if (device.contains("iPhone"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(scaling(2), 100)).takeScreenshot(driver);
        } else {
            logger.info("Taking screenshot of element:" + elementToBeTaken + " to compare...");
            WebElement el = driver.findElement(elementToBeTaken);
            //Using 'ashot' lib to take screenshot of the element.
            if (device.contains("desktop"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, el);
            else if (device.contains("android"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(scaling(3), 100)).coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, el);
            else if (device.contains("iPhone"))
                screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(scaling(2), 100)).coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver, el);
        }
        return screenshot;
    }

    /**
     * compareImages() method is used to take screenshot of an element or the entire page and compare with reference image if existed
     * If reference image does not exist, take a new screenshot and place it in Reference folder
     * Result of 'compareImages' function will be TRUE or FALSE:
     * TRUE means reference is updated or new screenshot matches reference image.
     * FALSE means new screenshot does not match reference image.
     *
     * @param folderName       is the name of folder that images will be stored
     * @param screenName       is the unique name of the image of the element or page
     * @param elementToBeTaken is the element that will be taken screenshot.
     * @return True or False based on result of pixel-by-pixel comparison between 2 images.
     * @throws Exception show any exception during the run.
     */
    public void compareImagesAshot(String folderName, String screenName, @Nullable By elementToBeTaken, double... threshold) throws Exception {
        //initialize a variable to store comparison result.
        String refPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/" + screenName + "_ref_";
        if (folderName.contains("desktop")) {
            refPath = refPath + "1440x892.jpg";
        } else if (folderName.contains("android")) {
            refPath = refPath + "360x640.jpg";
        } else if (folderName.contains("iPhone")) {
            refPath = refPath + "375x667.jpg";
        }
        String newPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/" + screenName + "_new_";
        if (folderName.contains("desktop")) {
            newPath = newPath + "1440x892.jpg";
        } else if (folderName.contains("android")) {
            newPath = newPath + "360x640.jpg";
        } else if (folderName.contains("iPhone")) {
            newPath = newPath + "375x667.jpg";
        }
        String diffPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/" + screenName + "_diff_";
        if (folderName.contains("desktop")) {
            diffPath = diffPath + "1440x892.jpg";
        } else if (folderName.contains("android")) {
            diffPath = diffPath + "360x640.jpg";
        } else if (folderName.contains("iPhone")) {
            diffPath = diffPath + "375x667.jpg";
        }

        //Create folders to store images if not exist
        logger.info("Checking folders...");
        File refFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references");
        File newFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new");
        File diffFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff");
        File diffHighlightedImage = new File(diffPath);
        File newImage = new File(newPath);

        //Check if the images folder is existed or not.
        folderCheck(refFolder);
        folderCheck(newFolder);
        folderCheck(diffFolder);
        if (diffHighlightedImage.exists()) {
            FileUtils.forceDelete(diffHighlightedImage);
        }
        if (newImage.exists()) {
            FileUtils.forceDelete(newImage);
        }

        //Check if reference image existed
        File ref = new File(refPath);
        //If it exists
        if (ref.exists()) {
            logger.info("References image existed!");
            //Setup a path for the new image

            logger.info("Taking new screenshot to compare...");
            //Take screenshot
            Screenshot actual = takeScreenshot(folderName, elementToBeTaken);
            //Render the image and save to New folder
            ImageIO.write(actual.getImage(), "PNG", new File(newPath));

            //Create a new File object to get the new image.
            File actualImage = new File(newPath);

            //ImageDiff object will make a comparison between new and ref images and highlight any unmatched pixel.
            ImageDiff diff = new ImageDiffer().makeDiff(ImageIO.read(ref), ImageIO.read(actualImage));
            BufferedImage diffImage = diff.getMarkedImage(); // comparison result with marked differences
            //Get the proportion of the different pixels
            double diffProportion = (double) diff.getDiffSize() / ((double) diffImage.getHeight() * (double) diffImage.getWidth());
            //Set up a threshold for differences.
            double th;
            if (threshold.length == 1) {
                th = threshold[0];
            } else {
                if (elementToBeTaken == null)
                    th = Double.parseDouble(getProperties("fullPageThreshold")) / 100;
                else th = Double.parseDouble(getProperties("elementThreshold")) / 100;
            }
            //If the proportion of different pixel is over the threshold, images are different.
            logger.info("Comparing images with threshold of " + th);
            if (diffProportion > th) {
                //Render a new image with highlighted different and placed in Diff folder
                ImageIO.write(diffImage, "png", diffHighlightedImage);
                throw new AssertionError("FAILED: Images " + screenName + " are different. Different percentage: " + String.format("%.2f", diffProportion * 100) + "% while threshold is " + th * 100 + "%");
            }
            //If the proportion of different pixel is under the threshold, images are the same.
            else {
                //Delete the image in New folder.
                FileUtils.forceDelete(actualImage);
                printSuccessMsg("PASSED: Images " + screenName + " are matched");
            }
        }
        //If reference image does not exist. Take a new screenshot and save to Reference folder
        else {
            //Create a new Screenshot object to store new screenshot.
            Screenshot r = takeScreenshot(folderName, elementToBeTaken);
            //Render image and placed in Reference folder
            ImageIO.write(r.getImage(), "PNG", new File(refPath));
            printSuccessMsg("This script is running for the first time, taking new screenshot as reference");
        }

    }

    public void compareImagesSB(String folderName, String screenName, @Nullable By elementToBeTaken, double... threshold) throws IOException {
        //initialize a variable to store comparison result.
        pause(3000);
        List<WebElement> images = driver.findElements(By.xpath("//div[@id='main']//img"));
        if (!images.isEmpty()) {
            for (WebElement image : images) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", image);
            }
        }
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0)");
        //Create folders to store images if not exist
        logger.info("Checking folders...");
        File refFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references");
        File newFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new");
        File diffFolder = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff");
        File diffHighlightedImage = null;
        if (folderName.contains("desktop")) {
            diffHighlightedImage = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/" + screenName + "_diff_1440x892.png");
        } else if (folderName.contains("android")) {
            diffHighlightedImage = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/" + screenName + "_diff_360x640.png");
        } else if (folderName.contains("iPhone")) {
            diffHighlightedImage = new File(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/" + screenName + "_diff_375x667.png");
        }

        //Check if the Reference folder is existed or not.
        folderCheck(refFolder);
        folderCheck(newFolder);
        folderCheck(diffFolder);
        if (diffHighlightedImage.exists()) {
            FileUtils.forceDelete(diffHighlightedImage);
        }

        //Check if reference image existed
        String refPath = null;
        if (folderName.contains("desktop")) {
            refPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/" + screenName + "_ref_1440x892.png";
        } else if (folderName.contains("android")) {
            refPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/" + screenName + "_ref_360x640.png";
        } else if (folderName.contains("iPhone")) {
            refPath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/" + screenName + "_ref_375x667.png";
        }
        File ref = new File(refPath);
        //If it exists
        if (ref.exists()) {
            logger.info("References image existed!");
            //Setup a path for the new image
            String filePath = null;
            if (folderName.contains("desktop")) {
                filePath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/" + screenName + "_new_1440x892.png";
            } else if (folderName.contains("android")) {
                filePath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/" + screenName + "_new_360x640.png";
            } else if (folderName.contains("iPhone")) {
                filePath = System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/" + screenName + "_new_375x667.png";
            }
            logger.info("Taking new screenshot to compare...");
            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollTo(0, 0)");
            //Check if an element or the entire page will be taken.
            if (elementToBeTaken == null) {
                logger.info("Taking screenshot of the entire page to compare...");
                //Using 'ashot' lib to take screenshot of the entire page
                if (folderName.contains("desktop"))
                    Shutterbug.shootPage(driver, Capture.FULL,
                            true)
                            .withName(screenName + "_new_1440x892")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
                else if (folderName.contains("android"))
                    Shutterbug.shootPage(driver,
                            Capture.FULL_SCROLL, 10,
                            true)
                            .withName(screenName + "_new_360x640")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
                else if (folderName.contains("iPhone"))
                    Shutterbug.shootPage(driver,
                            Capture.FULL_SCROLL, 100,
                            true)
                            .withName(screenName + "_new_375x667")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
            } else {
                logger.info("Taking screenshot of element:" + elementToBeTaken + " to compare...");
                WebElement el = driver.findElement(elementToBeTaken);
                scrollToElement(el);
                //Using 'ashot' lib to take screenshot of the element.
                if (folderName.contains("desktop"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL)
                            .withName(screenName + "_new_1440x892")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
                else if (folderName.contains("android"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL,
                            true)
                            .withName(screenName + "_new_360x640")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
                else if (folderName.contains("iPhone"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL,
                            true)
                            .withName(screenName + "_new_375x667")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/new/");
            }

            //Create a new File object to get the new image.
            File actualImage = new File(filePath);

            //ImageDiff object will make a comparison between new and ref images and highlight any unmatched pixel.
            ImageDiff diff = new ImageDiffer().makeDiff(ImageIO.read(ref), ImageIO.read(actualImage));
            BufferedImage diffImage = diff.getMarkedImage(); // comparison result with marked differences
            //Get the proportion of the different pixels
            double diffProportion = (double) diff.getDiffSize() / ((double) diffImage.getHeight() * (double) diffImage.getWidth());
            //Set up a threshold for differences.
            double th;
            if (threshold.length == 1) {
                th = threshold[0];
            } else {
                if (elementToBeTaken == null)
                    th = Double.parseDouble(getProperties("fullPageThreshold")) / 100;
                else th = Double.parseDouble(getProperties("elementThreshold")) / 100;
            }
            //If the proportion of different pixel is over the threshold, images are different.
            logger.info("Comparing images with threshold of " + th);
            if (diffProportion > th) {
                //Render a new image with highlighted different and placed in Diff folder
                ImageIO.write(diffImage, "png", diffHighlightedImage);
                throw new AssertionError("===FAILED: Images " + screenName + " are different. Different percentage: " + String.format("%.2f", diffProportion * 100) + "% while threshold is " + th * 100 + "%");
            }

            //If the proportion of different pixel is under the threshold, images are the same.
            else {
                //Delete the image in New folder.
                FileUtils.forceDelete(actualImage);
                //Set result to true
                printSuccessMsg("PASSED: Images " + screenName + " matched");
            }
        }
        //If reference image does not exist. Take a new screenshot and save to Reference folder
        else {
            ((JavascriptExecutor) driver)
                    .executeScript("window.scrollTo(0, 0)");
            //Check if an element or the entire page will be taken.
            if (elementToBeTaken == null) {
                logger.info("Taking screenshot of the entire page to save...");
                //Using 'ashot' lib to take screenshot of the entire page
                if (folderName.contains("desktop"))
                    Shutterbug.shootPage(driver, Capture.FULL, true)
                            .withName(screenName + "_ref_1440x892")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
                else if (folderName.contains("android"))
                    Shutterbug.shootPage(driver, Capture.FULL_SCROLL, 100,
                            true)
                            .withName(screenName + "_ref_360x640")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
                else if (folderName.contains("iPhone"))
                    Shutterbug.shootPage(driver, Capture.FULL_SCROLL, 100,
                            true)
                            .withName(screenName + "_ref_375x667")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
            } else {
                logger.info("Taking screenshot of element:" + elementToBeTaken + " to save...");
                WebElement el = driver.findElement(elementToBeTaken);
                //Using 'ashot' lib to take screenshot of the element.
                if (folderName.contains("desktop"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL)
                            .withName(screenName + "_ref_1440x892")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
                else if (folderName.contains("android"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL, true)
                            .withName(screenName + "_ref_360x640")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
                else if (folderName.contains("iPhone"))
                    Shutterbug.shootElement(driver, el, CaptureElement.FULL_SCROLL, true)
                            .withName(screenName + "_ref_375x667")
                            .save(System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/references/")
//                            .equalsWithDiff(ImageIO.read(ref), System.getProperty("user.dir") + "/images/" + getProperties("browser") + "/" + folderName + "/diff/")
                            ;
            }
            //Render image and placed in Reference folder
            printSuccessMsg("===This script is running for the first time, taking new screenshot as reference");
        }
    }

    public void folderCheck(File name) {
        if (!name.exists()) {
            //if not, create it
            if (name.mkdirs()) {
                logger.info("Directory " + name + " is created!");
            }
            //if there is an error, print a message.
            else {
                logger.info("Failed to create " + name + " directory!");
            }
        } else {
            logger.info("Directory " + name + " existed!");
        }
    }

    /**
     * hideElements() method is used to hide a specified element(s) from displaying.
     *
     * @param elements is the element(s) that need to be hidden
     */
    public void hideElements(By elements) {
        //Find all matched elements and put them in a list.
        List<WebElement> el = driver.findElements(elements);
        //IF list is not empty, hide all of the element in the list using Javascript Executor.
        if (!el.isEmpty()) {
            for (WebElement webElement : el) {
                logger.info("Hiding  " + webElement);
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", webElement);
            }
        }
        //If the list is empty, print a message.
        else logger.info("There is no matched element to hide.");
    }


    /**
     * showElements() method is used to make a specified element(s) visible.
     *
     * @param elements is the element(s) that need to be hidden
     */
    public void showElements(By elements) {
        //Find all matched elements and put it in a list.
        List<WebElement> el = driver.findElements(elements);
        //IF list is not empty, hide all of the element in the list using Javascript Executor.
        if (!el.isEmpty()) {
            for (WebElement webElement : el) {
                logger.info("Showing  " + webElement);
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='visible'", webElement);
            }
        }
        //If the list is empty, print a message.
        else logger.info("There is no matched element to show.");
    }

    /**
     * clickOnElement() method is used to click on a specified element.
     * Javascript Executor is used to click on Safari while normal method is used to click on Chrome
     *
     * @param element is the element(s) that need to be hidden
     */
    public void clickOnElement(WebElement element) {
        try {
            //Wait for element to be visible and clickable
            waitForElementToBeVisible(element, 30);
            waitForElementToBeClickable(element);
            scrollToElement(element);

            logger.info("Clicking on  " + element);
            //If browser is Safari, use JS Executor
            if (getProperties("browser").equals("Safari")) {
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                jse.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", element);
                logger.info("Clicked");
                pause(5000);
                //waitForPageToLoad(driver);
            }
            //If browser is Chrome or other, use normal method.
            else {
                element.click();
                logger.info("Clicked");
            }
        }
        //If element is overlapped by other element, throw an error message.
        catch (ElementClickInterceptedException e) {
            logger.info("Unable to click");
            throw e;
        }
    }

    public void clickOnElementAction(WebElement element) {
        try {

            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            logger.info("Clicked");
        }
        //If element is overlapped by other element, throw an error message.
        catch (ElementClickInterceptedException e) {
            logger.info("Unable to click");
            throw e;
        }
    }

    public void clickOnOneOfElementsJS(List<WebElement> elements, int index) {
        try {
            // Wait for element to be visible and clickable
            waitForElementToBeVisible(elements.get(index), 30);
            waitForElementToBeClickable(elements.get(index));

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", elements.get(index));
        }
        // If element is overlapped by other element, throw an error message.
        catch (ElementClickInterceptedException e) {
            logger.info("Unable to click");
            throw e;
        }
    }

    public void clickOnOneOfElements(List<WebElement> elements, int index) {
        try {
            // Wait for element to be visible and clickable
            waitForElementToBeVisible(elements.get(index), 30);
            waitForElementToBeClickable(elements.get(index));
            scrollToElement(elements.get(index));

            logger.info("Clicking on  " + elements.get(index));
            //If browser is Safari, use JS Executor
            if (getProperties("browser").equals("Safari")) {
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                jse.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", elements.get(index));
                logger.info("Clicked");
                pause(5000);
                //waitForPageToLoad(driver);
            }
            //If browser is Chrome or other, use normal method.
            else {
                elements.get(index).click();
                logger.info("Clicked");
            }
        }
        //If element is overlapped by other element, throw an error message.
        catch (ElementClickInterceptedException e) {
            logger.info("Unable to click on " + elements.get(index));
            throw e;
        } catch (IndexOutOfBoundsException ex) {
            logger.info("There is no matched element");
            throw ex;
        }
    }

    public void clickOnMultipleElements(WebElement[] elements) {
        try {
            for (WebElement element : elements) {
                scrollToElement(element);
                //If browser is Safari, use JS Executor
                if (getProperties("browser").equals("Safari")) {
                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    jse.executeScript("var elem=arguments[0]; setTimeout(function() {elem.click();}, 100)", element);
                    //waitForPageToLoad(driver);
                }
                //If browser is Chrome or other, use normal method.
                else {
                    element.click();
                }
                logger.info("Clicked");
            }
        }
        //If element is overlapped by other element, throw an error message.
        catch (ElementClickInterceptedException e) {
            logger.info("Unable to click on ");
            throw e;
        } catch (IndexOutOfBoundsException ex) {
            logger.info("There is no matched element");
        }
    }


    /**
     * typeIn() method is used to send text to a specified element.
     *
     * @param element is the element(s) that need to be hidden
     */
    public void typeIn(WebElement element, String text) {
        //Wait to element to be visible before interacting
        waitForElementToBeVisible(element, 10);
        logger.info("Typing \"" + text + "\" into " + element);
        //clear the field
        clearWebField(element);
        //Send text to the element.
        element.sendKeys(text);
    }

    private void clearWebField(WebElement element){
        while(!element.getAttribute("value").equals("")){
            element.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        }
    }

    /**
     * typeIn() method is used to send text to a specified element.
     *
     * @param element is the element(s) that need to be hidden
     */
    public void typeIn(WebElement element, Keys key) {
        // Wait to element to be visible before interacting
        waitForElementToBeVisible(element, 10);
        logger.info("Typing \"" + key + "\" into " + element);
        // clear the field
        element.clear();
        // Send text to the element.
        element.sendKeys(key);
    }


    /**
     * scrollToElement() method is used to scroll a specified element into view.
     *
     * @param element is the element(s) that need to be hidden
     */
    public void scrollToElement(WebElement element) {
        waitForElementToBeVisible(element, 10);
        Point p = element.getLocation();
        long innerHeight = (long) ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
        logger.info("Scrolling to " + element);
        ((JavascriptExecutor) driver).executeScript("window.scroll(" + p.getX() + "," + (p.getY() - innerHeight / 2) + ");", element);
    }

    /**
     * scrollToElement() method is used to scroll a specified element into view.
     *
     */
    public void scrollToTop() { 
        logger.info("Scrolling to top of the page");
        ((JavascriptExecutor) driver).executeScript("window.scroll(0,0)");
    }

    /**
     * openURL() method is used to open a URl with a restricted timeout of 5 seconds.
     * If timeout is reached, stop all loading requests and start interacting with the page.
     * Javascript Executor is used.
     *
     * @param url is the URL that need to be open
     */
    public void openURL(String url, int timeout) {
        String browser = getProperties("browser");
        driver.manage().timeouts().pageLoadTimeout(timeout, SECONDS);
        try {
            logger.info("Opening website at: " + url + " on " + browser);
            driver.get(url);
        } catch (TimeoutException e) {
            logger.info("Page Load Timeout reaches. Stopping all loading requests.");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("return window.stop");
        }
        logger.info("Opened website at: " + driver.getCurrentUrl() + " on " + browser);
        driver.manage().timeouts().pageLoadTimeout(Long.parseLong(getProperties("pageLoadTimeout")), SECONDS);

    }

    /**
     * hoverOnElement() method is used to hover on a specified element.
     * If browser is Safari, use JS Executor to action. If browser is Chrome or other, user Actions.
     *
     * @param element is the element(s) that need to be hovered on.
     */
    public void hoverOnElement(WebElement element) {
        //Check if browser is Safari
        if (getProperties("browser").equals("Safari")) {
            String javaScript = "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                    "arguments[0].dispatchEvent(evObj);";
            logger.info("Hovering on " + element);
            ((JavascriptExecutor) driver).executeScript(javaScript, element);
        }
        //If browser is Chrome or other.
        else {
            Actions actions = new Actions(driver);
            waitForElementToBeVisible(element, 30);
            logger.info("Hovering on " + element);
            actions.moveToElement(element).perform();
        }
    }

    /**
     * switchToTab() method is used to switch to other opened tab.
     * This action only run on Chrome. Safari will be handle differently.
     *
     * @param tabNumber is the tab that need to be switched to, start from 0.
     */
    public void switchToTab(int tabNumber) {
        //Get list of tab
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        try {
            //Switch to tab number.
            logger.info("Switching to tab number " + (tabNumber + 1));
            driver.switchTo().window(tabs.get(tabNumber));
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("There is only 1 tab. Cannot switch to other tab");
            throw e;
        }
    }

    /**
     * printErrorMsg() method is used to print error message with red text color.
     *
     * @param errorMsg is the message that need to be red.
     */
    public void printErrorMsg(String errorMsg) {
        printMessage("\033[0;31m", "===" + errorMsg);
    }

    /**
     * printSuccessMsg() method is used to print success message with green text color.
     *
     * @param successMsg is the message that need to be green.
     */
    public void printSuccessMsg(String successMsg) {
        printMessage("\033[0;32m", "===" + successMsg);
    }

    /**
     * printTestName() method is used to print Step name with teal text color.
     *
     * @param testName is the message that need to be teal.
     */
    public void printTestName(String testName) {
        printMessage("\033[0;36m", testName);
    }

    /**
     * printTestName() method is used to print Step name with teal text color.
     *
     * @param testStep is the message that need to be teal.
     */
    public void printTestStep(String testStep) {
        printMessage("\033[0;33m", testStep);
    }

    public void printMessage(String color, String message) {
        String path = System.getProperty("user.dir") + "/src/test/resources/log4j.properties";
        if (loadProperties(path, "log4j.rootLogger").contains("console")) {
            logger.info(color + message + "\033[0m");
        } else {
            logger.info(message);
            System.out.println(color + message + "\033[0m");
        }
    }
    

    public void removeElement(WebElement[] elements){
        if (!(elements.length == 0)) {
            for (WebElement webElement : elements) {
                logger.info("Removing element: " + webElement);
                ((JavascriptExecutor) driver).executeScript("arguments[0].parentNode.removeChild(arguments[0])", webElement);
            }
        } else logger.warn("There is no element to remove");
    }

    public void selectFromDropDown(WebElement element, String option) {
        scrollToElement(element);
        logger.info("Selecting " + option + " from dropdown " + element);
        Select dropDown = new Select(element);
        try {
            dropDown.selectByVisibleText(option);
        } catch (NoSuchElementException e) {
            dropDown.selectByValue(option);
        }
    }

    public void selectFromDropDown(WebElement element, int index) {
        scrollToElement(element);
        logger.info("Selecting " + index + " from dropdown " + element);
        Select dropDown = new Select(element);
        dropDown.selectByIndex(index);
    }

    public String getDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        return formatter.format(date);
    }

    public void switchFrame(String id) {
        logger.info("Switching to " + id + " frame");
        if (!id.equals("defaultContent"))
            driver.switchTo().frame(id);
        else driver.switchTo().defaultContent();
    }

    public void scrollToBottom(){
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public boolean urlCheck(String url) throws InterruptedException {
        boolean urlStatus=false;
        for(int x=0;x<15; x++){
            if(!driver.getCurrentUrl().contains(url)) {
                MILLISECONDS.sleep(500);
            } else{ urlStatus= true;
                break;}
        }
        return urlStatus;
    }

    public String getRandomString(int size)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
    //endregion
}
