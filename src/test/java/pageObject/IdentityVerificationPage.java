package pageObject;

import helper.GeneralMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class IdentityVerificationPage {
    WebDriver driver;
    GeneralMethods gm;

    public IdentityVerificationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    @FindAll({
            @FindBy (css = "[role='checkbox']")
    })
    private List<WebElement> checkboxes;

    @FindAll({
            @FindBy (css = "[role='radio']")
    })
    private List<WebElement> radioBtn;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement continueBtn;

    @FindBy (xpath = "//div[@class='aspire-cta-box aspire-cta']/div")
    private WebElement uploadBtn;

    @FindBy (css = ".q-dialog button")
    private WebElement popupBtn;

    @FindBy (css = "button input[type='file']")
    private WebElement documentUpload;

    public List<WebElement> getCheckboxes() {
        return checkboxes;
    }

    public List<WebElement> getRadioBtn() {
        return radioBtn;
    }

    public WebElement getContinueBtn() {
        return continueBtn;
    }

    public WebElement getUploadBtn() {
        return uploadBtn;
    }

    public WebElement getPopupBtn() {
        return popupBtn;
    }

    public WebElement getDocumentUpload() {
        return documentUpload;
    }
}
