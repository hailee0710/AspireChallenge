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
import java.util.ArrayList;
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

    @FindAll({
            @FindBy(css = "[role='radio']")
    })
    private List<WebElement> radioButtons;

    @FindBy (css = ".onboarding-step-business-relationship__question-icon")
    private WebElement PEPicon;

    @FindBy (css = ".onboarding-step-politically-exposed-person-view__text-title")
    private WebElement PEPtitle;

    @FindBy (css = "input[type='file']")
    private WebElement documentUpload;

    @FindBy (css = ".onboarding-step-certificates-of-shareholding__skip-button")
    private WebElement skipBtn;

    @FindBy (css = "[label='What will you use the account for?'] label")
    private WebElement accountUseSelect;

    @FindBy (css = "[label='How is your business funded?'] label")
    private WebElement businessFundedSelect;

    @FindAll({
            @FindBy(css = ".chip-items__item-inner")
    })
    private List<WebElement> chipItems;

    @FindBy (css = "[label='If others, please specify the source of funds'] input")
    private WebElement otherFundsField;

    @FindBy (css = "[role='checkbox']")
    private WebElement sameCheckBox;

    @FindBy (css = "input[data-cy='address-form-address']")
    private WebElement mainAddressField;

    @FindBy (css = "input[data-cy='address-form-locality']")
    private WebElement cityField;

    @FindBy (css = "[data-cy='address-form-administrative_area'] [data-cy='address-form-administrative_area']")
    private WebElement provinceSelect;

    @FindBy (css = "input[data-cy='address-form-postcode']")
    private WebElement postcodeField;

    @FindBy (css = "[data-cy='address-form-country'] [data-cy='address-form-country']")
    private WebElement countrySelect;

    public WebElement getOtherFundsField() {
        return otherFundsField;
    }

    public WebElement getSameCheckBox() {
        return sameCheckBox;
    }

    public WebElement getMainAddressField() {
        return mainAddressField;
    }

    public WebElement getCityField() {
        return cityField;
    }

    public WebElement getProvinceSelect() {
        return provinceSelect;
    }

    public WebElement getPostcodeField() {
        return postcodeField;
    }

    public WebElement getCountrySelect() {
        return countrySelect;
    }

    public WebElement getAccountUseSelect() {
        return accountUseSelect;
    }

    public WebElement getBusinessFundedSelect() {
        return businessFundedSelect;
    }

    public List<WebElement> getChipItems() {
        return chipItems;
    }

    public WebElement getSkipBtn() {
        return skipBtn;
    }

    public List<WebElement> getBusinessModelFields() {
        return businessModelFields;
    }

    public List<WebElement> getBusinessModelFieldsError() {
        return businessModelFieldsError;
    }

    public WebElement getWebsiteURL() {
        return websiteURL;
    }

    public WebElement getWebsiteURLError() {
        return websiteURLError;
    }

    public WebElement getNoWebsiteCheckbox() {
        return noWebsiteCheckbox;
    }

    public WebElement getCompanySizeSelect() {
        return companySizeSelect;
    }

    public WebElement getAnnualAvenueSelect() {
        return annualAvenueSelect;
    }

    public WebElement getMonthlySpendSelect() {
        return monthlySpendSelect;
    }

    public List<WebElement> getRadioButtons() {
        return radioButtons;
    }

    public WebElement getPEPicon() {
        return PEPicon;
    }

    public WebElement getPEPtitle() {
        return PEPtitle;
    }

    public WebElement getDocumentUpload() {
        return documentUpload;
    }

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
        while (listItems.size() == 0) {
            entityCategorySelect.click();
        }
        gm.clickOnOneOfElements(listItems, entityCategory);
        gm.clickOnElement(entityTypeSelect);
        while (listItems.size() == 0) {
            entityTypeSelect.click();
        }
        gm.clickOnOneOfElements(listItems, entityType);
        gm.typeIn(UENField, uen);
        gm.clickOnElement(industrySelect);
        while (listItems.size() == 0) {
            industrySelect.click();
        }
        gm.clickOnOneOfElements(listItems, industry);
        gm.clickOnElement(subIndustrySelect);
        while (listItems.size() == 0) {
            subIndustrySelect.click();
        }
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
        while (listItems.size() == 0) {
            companySizeSelect.click();
        }
        gm.clickWithJavaExecuter(listItems.get(companySize));
        gm.clickOnElement(annualAvenueSelect);
        while (listItems.size() == 0) {
            annualAvenueSelect.click();
        }
        gm.clickWithJavaExecuter(listItems.get(revenue));
        gm.clickOnElement(monthlySpendSelect);
        while (listItems.size() == 0) {
            monthlySpendSelect.click();
        }
        gm.clickWithJavaExecuter(listItems.get(monthlySpend));
    }

    public void select3rdForm(int pep, int shareholders) {
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnOneOfElements(radioButtons, pep);
        gm.clickOnOneOfElements(radioButtons, shareholders);
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
        gm.waitForElementToNotExist(loader, 30);
    }

    public void uploadCertificate(String path) {
        gm.waitForElementToBeVisible(skipBtn, 30);
        documentUpload.sendKeys(path);
    }

    public void selectAnOption(WebElement element, int index){
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(element);
        while (listItems.size() == 0) {
            element.click();
        }
        gm.clickWithJavaExecuter(listItems.get(index));
        gm.clickOnElement(element);
    }

    public void selectAllOptions(WebElement element){
        gm.pause(500);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(element);
        while (listItems.size() == 0) {
            element.click();
        }
        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<String> chipNames = new ArrayList<>();
        for(WebElement item : listItems) {
            gm.clickWithJavaExecuter(item);
            itemNames.add(item.getText());
        }
        element.click();
        for (WebElement chip : chipItems) {
            chipNames.add(chip.getText());
        }
        System.out.println(itemNames);
        System.out.println(chipNames);
        Assert.assertNotEquals(0, itemNames.size());
        Assert.assertNotEquals(0, chipNames.size());
        Assert.assertEquals("selected items should be displayed", itemNames, chipNames);
        Assert.assertTrue("Other field should be displayed", otherFundsField.isDisplayed());
    }

    public void fillInLastForm(boolean same, String...args) {
        if (same)
            gm.clickOnElement(sameCheckBox);
        else {
            gm.typeIn(mainAddressField, args[0]);
            gm.typeIn(cityField, args[1]);
            gm.clickOnElement(provinceSelect);
            while (listItems.size() == 0) {
                provinceSelect.click();
            }
            gm.clickWithJavaExecuter(listItems.get(Integer.parseInt(args[2])));
            gm.typeIn(postcodeField, args[3]);
        }
    }


}
