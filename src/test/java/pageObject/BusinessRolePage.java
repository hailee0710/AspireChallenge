package pageObject;

import helper.GeneralMethods;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class BusinessRolePage {
    WebDriver driver;
    GeneralMethods gm;

    public BusinessRolePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    private final By loader = By.cssSelector(".aspire-loader");

    @FindBys({
        @FindBy (css = ".business-role-step-role-selector__option-wrapper .business-role-step-role-selector__role-card")
    })
    private List<WebElement> roles;

    @FindBy (css = ".country-select__wrapper label input")
    private WebElement countrySelect;

    @FindAll({
            @FindBy (css = "[role='listbox'] .q-virtual-scroll__content > div")
    })
    private List<WebElement> listItems;

    @FindBy (css = "div[role='checkbox']")
    private WebElement registeredCheckBox;

    @FindBy (css = ".aspire-field .q-select--multiple")
    private WebElement solutionSelect;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement continueBtn;

    @FindBys({
            @FindBy (css = "[role='radiogroup'] [role='radio']")
    })
    private List<WebElement> registeredBusiness;

    @FindAll({
            @FindBy(css = ".chip-items__item-inner")
    })
    private List<WebElement> chipItems;

    @FindBy (css = ".aspire-header__back-button")
    private WebElement backBtn;

    @FindBy (css = ".aspire-cta-screen__title")
    private WebElement screenTitle;

    public List<WebElement> getRoles() {
        return roles;
    }

    public WebElement getCountrySelect() {
        return countrySelect;
    }

    public List<WebElement> getListItems() {
        return listItems;
    }

    public WebElement getRegisteredCheckBox() {
        return registeredCheckBox;
    }

    public WebElement getSolutionSelect() {
        return solutionSelect;
    }

    public WebElement getContinueBtn() {
        return continueBtn;
    }

    public List<WebElement> getRegisteredBusiness() {
        return registeredBusiness;
    }

    public List<WebElement> getChipItems() {
        return chipItems;
    }

    public WebElement getBackBtn() {
        return backBtn;
    }

    public void goNext() {
        gm.clickOnElement(continueBtn);
    }

    public String getScreenTitle() {
        gm.waitForElementToBeVisible(screenTitle, 30);
        return screenTitle.getText();
    }

    public void directorSelect() {
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(roles.get(0));
        gm.waitForElementToBeVisible(countrySelect, 30);
        gm.waitForElementToBeVisible(solutionSelect, 30);
    }

    public void employeeSelect() {
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(roles.get(1));
        gm.waitForElementToBeVisible(countrySelect, 30);
        gm.waitForElementToBeVisible(solutionSelect, 30);
    }

    public void freelancerSelect() {
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(roles.get(2));
        gm.waitForElementToBeVisible(solutionSelect, 30);
        gm.waitForElementToBeVisible(registeredBusiness.get(0), 30);
        gm.waitForElementToBeVisible(registeredBusiness.get(1), 30);
    }

    public void checkNotRegister() {
        gm.clickOnElement(registeredCheckBox);
        String state = countrySelect.getAttribute("aria-disabled");
        Assert.assertEquals("Country select should be disabled", "true", state);
        gm.clickOnElement(solutionSelect);
        String chipName = listItems.get(1).getText();
        gm.clickOnOneOfElements(listItems, 1);
        gm.clickOnElement(solutionSelect);
        Assert.assertEquals("Selected chip should be displayed", chipName, chipItems.get(0).getText());
        Assert.assertTrue("Continue button should be enabled", continueBtn.isEnabled());
    }

    public void checkFreelancerOptions() {
        gm.pause(500);
        solutionSelect.click();
        String chipName = listItems.get(1).getText();
        gm.clickOnOneOfElements(listItems, 1);
        solutionSelect.click();
        Assert.assertEquals("Selected chip should be displayed", chipName, chipItems.get(0).getText());
        gm.clickOnOneOfElements(registeredBusiness, 0);
        Assert.assertTrue("Select should be displayed", countrySelect.isDisplayed());
        backBtn.click();
        freelancerSelect();
        gm.clickOnOneOfElements(registeredBusiness, 1);
        Assert.assertTrue("Select should be displayed", countrySelect.isDisplayed());
    }

    public void selectASolution(int index){
        gm.pause(1000);
        gm.waitForElementToNotExist(loader, 30);
        gm.clickOnElement(solutionSelect);
        gm.clickWithJavaExecuter(listItems.get(index));
        gm.clickOnElement(solutionSelect);
    }

    public void selectAllSolutions(){
        gm.pause(500);
        gm.waitForElementToNotExist(loader, 30);
        solutionSelect.click();
        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<String> chipNames = new ArrayList<>();
        for(WebElement item : listItems) {
            gm.clickWithJavaExecuter(item);
            itemNames.add(item.getText());
        }
        solutionSelect.click();
        for (WebElement chip : chipItems) {
            chipNames.add(chip.getText());
        }
        System.out.println(itemNames);
        System.out.println(chipNames);
        Assert.assertNotEquals(0, itemNames.size());
        Assert.assertNotEquals(0, chipNames.size());
        Assert.assertEquals("selected items should be displayed", itemNames, chipNames);
    }

    public void successPath() {
        directorSelect();
        selectASolution(0);
        goNext();
    }

}
