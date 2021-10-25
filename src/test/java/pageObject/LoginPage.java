package pageObject;

import helper.GeneralMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LoginPage {
    WebDriver driver;
    GeneralMethods gm;
    public LoginPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm  = new GeneralMethods(driver);
    }

    @FindBy (css = "input[name='username']")
    private WebElement emailPhoneField;

    @FindBy (css = "input[type='checkbox']")
    private WebElement rememberMeCheckBox;

    @FindBy (xpath = "//a[contains(@class, 'login-step-start__register-link')]")
    private WebElement registerLink;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement nextBtn;

    @FindBy (css = ".q-dialog.q-dialog--modal")
    private WebElement countryCodeDialog;

    @FindBy (css = ".phone-input__input")
    private WebElement dialogPhoneField;

    @FindBy (css = ".flag-select__icon")
    private WebElement changeCountryBtn;

    @FindBys ({
            @FindBy(css = "[role='listbox'] .q-virtual-scroll__content > div")
    })
    private List<WebElement> countryListItems;

    @FindBy (css = ".q-dialog.q-dialog--modal button")
    private WebElement dialogNextBtn;

    public WebElement getEmailPhoneField() {
        return emailPhoneField;
    }

    public WebElement getRememberMeCheckBox() {
        return rememberMeCheckBox;
    }

    public WebElement getRegisterLink() {
        return registerLink;
    }

    public WebElement getNextBtn() {
        return nextBtn;
    }

    public WebElement getCountryCodeDialog() {
        return countryCodeDialog;
    }

    public WebElement getDialogPhoneField() {
        return dialogPhoneField;
    }

    public WebElement getChangeCountryBtn() {
        return changeCountryBtn;
    }

    public List<WebElement> getCountryListItems() {
        return countryListItems;
    }

    public WebElement getDialogNextBtn() {
        return dialogNextBtn;
    }

    public void goToRegisterPage(String url) {
        gm.openURL(url, 30);
        gm.clickOnElement(registerLink);
    }

    public void enterPhoneNumber(String phone) {
        gm.typeIn(emailPhoneField, phone);
        gm.clickOnElement(nextBtn);
    }

    public void chooseCountryCode(int index) {
        gm.clickOnOneOfElements(countryListItems, index);
        gm.clickOnElement(dialogNextBtn);
    }
}
