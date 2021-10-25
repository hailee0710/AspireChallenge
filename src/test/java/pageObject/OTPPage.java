package pageObject;

import helper.GeneralMethods;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OTPPage {
    WebDriver driver;
    GeneralMethods gm;

    public OTPPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    private final By loader = By.cssSelector(".aspire-loader");

    @FindBy (css = ".verify-otp-form__recipient")
    private WebElement phoneOrEmail;

    @FindBy (css = "[data-cy='digit-input']")
    private WebElement otpInput;

    @FindBy (css = "form .aspire-label__text.text-negative")
    private WebElement otpErrorMsg;

    @FindBy (css = "form button")
    private WebElement resendOtpBtn;

    @FindBy (css = ".aspire-cta-screen__content > p")
    private WebElement successMsg;

    @FindBy (css = ".aspire-cta-box.aspire-cta button")
    private WebElement continueBtn;

    @FindBy (css = ".q-notification__message")
    private WebElement toastMsg;

    public By getLoader() {
        return loader;
    }

    public WebElement getOtpInput() {
        return otpInput;
    }

    public WebElement getResendOtpBtn() {
        return resendOtpBtn;
    }

    public WebElement getContinueBtn() {
        return continueBtn;
    }

    public WebElement getToastMsg() {
        return toastMsg;
    }

    public void enterOTP(String otp) {
        gm.waitForElementToNotExist(loader, 30);
        gm.waitForElementToBeVisible(phoneOrEmail, 10);
        gm.pause(2000);
        otpInput.sendKeys(otp);
        gm.waitForElementToNotExist(loader, 30);
    }

    public String getOtpErrorMsg() {
        gm.waitForElementToBeVisible(otpErrorMsg, 30);
        return otpErrorMsg.getText();
    }

    public String getSuccessMsg() {
        gm.waitForElementToBeVisible(successMsg, 30);
        return successMsg.getText();
    }

    public String getPhoneOrEmail() {
        gm.waitForElementToBeVisible(phoneOrEmail, 30);
        return phoneOrEmail.getText();
    }

    public String getToastMessage() {
        gm.waitForElementToBeVisible(toastMsg, 30);
        return toastMsg.getText();
    }

    public void validateErrorMsg(String errMsg) {
        gm.waitForElementToBeVisible(phoneOrEmail, 30);
        String otpErrorMsg = getOtpErrorMsg();
        Assert.assertEquals("Error message should be displayed on Phone field", errMsg, otpErrorMsg);
    }

    public void resendOTP(String resendMsg) {
        gm.waitForElementToBeVisible(phoneOrEmail, 10);
        String phone = getPhoneOrEmail();
        resendMsg += " " + phone + ".";
        gm.clickOnElement(resendOtpBtn);
        String toastMsg = getToastMessage();
        Assert.assertEquals("Toast message should be displayed", resendMsg, toastMsg);
        gm.pause(30000);
        Assert.assertTrue("Resend button should be available after 30s", resendOtpBtn.isDisplayed());
    }

    public void goNext() {
        gm.clickOnElement(continueBtn);
    }
}
