package pageObject;

import helper.GeneralMethods;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.annotation.Nullable;
import java.util.List;

public class BusinessDetailsPage {
    WebDriver driver;
    GeneralMethods gm;

    public BusinessDetailsPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    private final By loader = By.cssSelector(".aspire-loader");

    @FindBy (css = "input[data-cy='register-business-name']")
    private WebElement businessNameField;

    @FindBy (css = "input[data-cy='register-business-registration-type']")
    private WebElement entityCategorySelect;

    @FindBy (css = "input[data-cy='register-business-sub-registration-type']")
    private WebElement entityTypeSelect;

    @FindBy (css = "input[data-cy='register-business-registration-number']")
    private WebElement UENField;

    @FindBy (css = "[data-cy='register-business-registration-number'] .aspire-label__text.text-negative")
    private WebElement UENFieldErrorMsg;

    @FindAll({
            @FindBy (css = "[role='listbox'] .q-virtual-scroll__content > div")
    })
    private List<WebElement> listItems;

    @FindBy (css = "input[data-cy='register-business-industry']")
    private WebElement industrySelect;

    @FindBy (css = "input[data-cy='register-business-sub-industry']")
    private WebElement subIndustrySelect;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement continueBtn;

    @FindAll({
            @FindBy(css = "input[data-cy='register-business-registration-business-model']")
    })
    private List<WebElement> businessModelFields;

    @FindAll({
            @FindBy(css = "[data-cy='register-business-registration-business-model'] .aspire-label__text.text-negative")
    })
    private List<WebElement> businessModelFieldsError;

    @FindBy (css = "input[data-cy='register-business-registration-website-url']")
    private WebElement websiteURL;

    @FindBy (css = "[data-cy='register-business-registration-website-url'] .aspire-label__text.text-negative")
    private WebElement websiteURLError;

    @FindBy (xpath = "//div[@data-cy='register-business-registration-website-url']/following-sibling::div[@role='checkbox']")
    private WebElement noWebsiteCheckbox;

    @FindBy (css = "input[placeholder='Select your company size']")
    private WebElement companySizeSelect;

    @FindBy (css = "input[placeholder='Select your annual revenue']")
    private WebElement annualAvenueSelect;

    @FindBy (css = "input[placeholder='Select your total monthly card spend']")
    private WebElement monthlySpendSelect;

    public WebElement getBusinessNameField() {
        return businessNameField;
    }

    public WebElement getEntityCategorySelect() {
        return entityCategorySelect;
    }

    public WebElement getEntityTypeSelect() {
        return entityTypeSelect;
    }

    public WebElement getUENField() {
        return UENField;
    }

    public List<WebElement> getListItems() {
        return listItems;
    }

    public WebElement getIndustrySelect() {
        return industrySelect;
    }

    public WebElement getSubIndustrySelect() {
        return subIndustrySelect;
    }

    public WebElement getUENFieldErrorMsg() {
        return UENFieldErrorMsg;
    }

    public WebElement getContinueBtn() {
        return continueBtn;
    }

    public void fillIn1stForm(
            String businessName,
            int entityCategory,
            int entityType,
            String uen,
            int industry,
            int subIndustry
    ) {
        gm.waitForElementToNotExist(loader, 30);
        gm.typeIn(businessNameField, businessName);
        gm.clickOnElement(entityCategorySelect);
        gm.clickOnOneOfElements(listItems, entityCategory);
        gm.clickOnElement(entityTypeSelect);
        gm.clickOnOneOfElements(listItems, entityType);
        gm.typeIn(UENField, uen);
        gm.clickOnElement(industrySelect);
        gm.clickOnOneOfElements(listItems, industry);
        gm.clickOnElement(subIndustrySelect);
        gm.clickOnOneOfElements(listItems, subIndustry);
    }

    public void fillIn2ndForm(
            String businessActivity,
            String detail,
            String websiteUrl,
            boolean haveWebsite,
            int companySize,
            int revenue,
            int monthlySpend
    ) {
        gm.waitForElementToNotExist(loader, 30);
        gm.typeIn(businessModelFields.get(0), businessActivity);
        gm.typeIn(businessModelFields.get(1), detail);
        if (haveWebsite)
            gm.typeIn(websiteURL, websiteUrl);
        else
            gm.clickOnElement(noWebsiteCheckbox);
        gm.clickOnElement(companySizeSelect);
        gm.clickWithJavaExecuter(listItems.get(companySize));
        gm.clickOnElement(annualAvenueSelect);
        gm.clickWithJavaExecuter(listItems.get(revenue));
        gm.clickOnElement(monthlySpendSelect);
        gm.clickWithJavaExecuter(listItems.get(monthlySpend));
    }

    public void validateUENErrorMsg(String expected) {
        String errorMsg = UENFieldErrorMsg.getText();
        Assert.assertEquals("Correct Error message should be displayed", expected, errorMsg);
    }

    public void validate3ErrorMsg(@Nullable String businessActivityError, @Nullable String detailsError, @Nullable String websiteError) {
        String errorMsg;
        if (businessActivityError != null) {
            errorMsg = businessModelFieldsError.get(0).getText();
            Assert.assertEquals("Correct Error message should be displayed", businessActivityError, errorMsg);
        }
        if (detailsError != null) {
            errorMsg = businessModelFieldsError.get(1).getText();
            Assert.assertEquals("Correct Error message should be displayed", detailsError, errorMsg);
        }

        if (websiteError != null) {
            errorMsg = websiteURLError.getText();
            Assert.assertEquals("Correct Error message should be displayed", websiteError, errorMsg);
        }
    }

    public void goNext() {
        gm.clickOnElement(continueBtn);
    }


}
