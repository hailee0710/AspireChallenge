package pageObject;

import helper.GeneralMethods;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.List;

public class RegistrationPage {

    WebDriver driver;
    GeneralMethods gm;

    public RegistrationPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    @FindBy (css = "input[data-cy='register-person-name']")
    private WebElement fullNameField;

    @FindBy (css = "input[data-cy='register-person-preferred-name']")
    private WebElement preferredNameField;

    @FindBy (css = "input[data-cy='register-person-email']")
    private WebElement emailField;

    @FindBy (css = "[data-cy='register-person-email'] .aspire-label__text.text-negative")
    private WebElement emailErrorMsg;

    @FindBy (css = "input[data-cy='register-person-phone']")
    private WebElement phoneField;

    @FindBy (css = "[data-cy='register-person-phone'] .aspire-label__text.text-negative")
    private WebElement phoneErrorMsg;

    @FindBy (css = ".flag-select__icon")
    private WebElement changeCountryBtn;

    @FindAll({
            @FindBy (css = "[role='listbox'] .q-virtual-scroll__content > div")
    })
    private List<WebElement> listItems;

    @FindBy (css = "input[data-cy='register-person-heard-about']")
    private WebElement heardAboutSelect;

    @FindBy (css = "input[data-cy='register-person-referral-code']")
    private WebElement referalField;

    @FindBy (css = "[data-cy='register-person-referral-code'] button")
    private WebElement applyCode;

    @FindBy (css = "[data-cy='register-person-referral-code'] .referral-code-input__referral-information-wrapper .aspire-label__text")
    private WebElement referalCodeError;

    @FindBy (css = "[data-cy='register-person-privacy'] ")
    private WebElement termsCheckBox;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement continueBtn;

    @FindBy (css = ".register-step-person__login span")
    private WebElement backToLogin;

    @FindBy (css = ".q-notification__message")
    private WebElement errorToast;

    @FindBy (css = ".q-dialog.q-dialog--modal a")
    private WebElement loginBtn;

    @FindBy (css = ".q-dialog.q-dialog--modal img")
    private WebElement closeBtn;

    public WebElement getFullNameField() {
        return fullNameField;
    }

    public WebElement getPreferredNameField() {
        return preferredNameField;
    }

    public WebElement getEmailField() {
        return emailField;
    }

    public WebElement getPhoneField() {
        return phoneField;
    }

    public WebElement getChangeCountryBtn() {
        return changeCountryBtn;
    }

    public List<WebElement> getListItems() {
        return listItems;
    }

    public WebElement getHeardAboutSelect() {
        return heardAboutSelect;
    }

    public WebElement getReferalField() {
        return referalField;
    }

    public WebElement getApplyCode() {
        return applyCode;
    }

    public WebElement getReferalCodeError() {
        return referalCodeError;
    }

    public WebElement getTermsCheckBox() {
        return termsCheckBox;
    }

    public WebElement getContinueBtn() {
        return continueBtn;
    }

    public WebElement getBackToLogin() {
        return backToLogin;
    }

    public WebElement getErrorToast() {
        return errorToast;
    }

    public WebElement getLoginBtn() {
        return loginBtn;
    }

    public WebElement getCloseBtn() {
        return closeBtn;
    }

    public void fillInForm(
            String fullName,
            @Nullable String preferredName,
            String email,
            @Nullable String phone,
            int country,
            int heardAbout,
            @Nullable String referralCode,
            boolean acceptTerms
    ) {
        gm.waitForElementToBeVisible(fullNameField, 30);
        fullNameField.clear();
        gm.typeIn(fullNameField, fullName);
        if (preferredName != null)
            gm.typeIn(preferredNameField, preferredName);
        gm.typeIn(emailField, email);
        if (phone != null)
            gm.typeIn(phoneField, phone);
        if (country != -1) {
            gm.clickOnElement(changeCountryBtn);
            gm.clickOnOneOfElements(listItems, country);
        }
        if (heardAbout != -1) {
            gm.clickOnElement(heardAboutSelect);
            gm.clickOnOneOfElements(listItems, heardAbout);
        }
        if (referralCode != null) {
            gm.typeIn(referalField, referralCode);
            gm.clickOnElement(applyCode);
        }
        if (acceptTerms)
            gm.clickOnElement(termsCheckBox);
    }

    public void goNext(){
        gm.clickOnElement(continueBtn);
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg.getText();
    }

    public String getPhoneErrorMsg() {
        return phoneErrorMsg.getText();
    }

    public String getReferalErrorMsg() {
        gm.waitForElementToBeVisible(referalCodeError, 10);
        return referalCodeError.getText();
    }

    public String getToastMsg() {
        gm.waitForElementToBeVisible(errorToast, 30);
        return errorToast.getText();
    }

    public String getButtonState() {
        return continueBtn.getAttribute("class");
    }

    public void checkExistingDialog() {
        Assert.assertTrue("Login popup should be displayed", loginBtn.isDisplayed());
    }
    public void backtoLogin() {
        gm.clickOnElement(backToLogin);
    }

    public void validateErrorMsg(@Nullable String phoneErrMsg, @Nullable String emailErrMsg) {
        if (phoneErrMsg != null) {
            String phoneErrorMsg = getPhoneErrorMsg();
            Assert.assertEquals("Error message should be displayed on Phone field", phoneErrMsg, phoneErrorMsg);
        }
        if (emailErrMsg != null) {
            String emailErrorMsg = getEmailErrorMsg();
            Assert.assertEquals("Error message should be displayed on email field", emailErrMsg, emailErrorMsg);
        }
    }

    public String validRegister(String validEmail, String validUserName) {
        String phone = String.valueOf(Instant.now().getEpochSecond());
        String email = String.format(validEmail, phone);

        gm.printTestStep("Pre: Enter all correct info in register page");
        fillInForm(validUserName, null, email, phone, 1, 1,  null, true);
        goNext();
        return email;
    }

}
