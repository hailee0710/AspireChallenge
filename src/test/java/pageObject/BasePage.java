package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    WebDriver driver;

    public BasePage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy (tagName = "header")
    WebElement header;

    @FindBy (tagName = "body")
    WebElement body;

    @FindBy (tagName = "footer")
    WebElement footer;

}
