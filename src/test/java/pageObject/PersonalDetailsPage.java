package pageObject;

import helper.GeneralMethods;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class PersonalDetailsPage {
    WebDriver driver;
    GeneralMethods gm;

    public PersonalDetailsPage (WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        gm = new GeneralMethods(driver);
    }

    @FindAll({
            @FindBy (css = ".auth-form__card .onboarding-step-register-select-method__column-content button")
    })
    private List<WebElement> getStartedBtns;

    @FindBy (css = "input[data-cy='person-edit-email']")
    private WebElement emailField;

    @FindBy (css = "[data-cy='person-edit-email'] .aspire-label__text.text-negative")
    private WebElement emailErrorMsg;

    @FindBy (xpath = "//div[@label='Date of birth']//input")
    private WebElement dobField;

    @FindBy (xpath = "//div[@label='Date of birth']//div[@class='aspire-label__text text-negative']")
    private WebElement dobErrorMsg;

    @FindAll({
            @FindBy(css = ".q-date__navigation button.q-btn--rectangle")
    })
    private List<WebElement> monthYearBtn;

    @FindAll({
            @FindBy(css = ".q-date__months-item span.block")
    })
    private List<WebElement> months;

    @FindAll({
            @FindBy(css = ".q-date__years-item span.block")
    })
    private List<WebElement> years;

    @FindAll({
            @FindBy (css = ".q-date__view.q-date__years button.q-btn--round")
    })
    private List<WebElement> changeYearView;

    @FindAll({
            @FindBy(css = ".q-date__calendar-days-container button span.block")
    })
    private List<WebElement> dates;

    @FindBy (css = "input[url='countries/all']")
    private WebElement countrySelect;

    @FindAll({
            @FindBy (css = "[role='listbox'] .q-virtual-scroll__content > div")
    })
    private List<WebElement> listItems;

    @FindBy (css = "input[data-cy='kyc-gender']")
    private WebElement genderSelect;

    @FindBy (css = ".auth-form__buttons button")
    private WebElement submitBtn;

    @FindBy (css = ".kyc-header__progress-percentage-badge")
    private WebElement progressBadge;

    public List<WebElement> getGetStartedBtns() {
        return getStartedBtns;
    }

    public WebElement getEmailField() {
        return emailField;
    }

    public WebElement getDobField() {
        return dobField;
    }

    public List<WebElement> getMonthYearBtn() {
        return monthYearBtn;
    }

    public List<WebElement> getMonths() {
        return months;
    }

    public List<WebElement> getYears() {
        return years;
    }

    public List<WebElement> getChangeYearView() {
        return changeYearView;
    }

    public List<WebElement> getDates() {
        return dates;
    }

    public WebElement getCountrySelect() {
        return countrySelect;
    }

    public List<WebElement> getListItems() {
        return listItems;
    }

    public WebElement getGenderSelect() {
        return genderSelect;
    }

    public WebElement getSubmitBtn() {
        return submitBtn;
    }

    public WebElement getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public WebElement getDobErrorMsg() {
        return dobErrorMsg;
    }

    public WebElement getProgressBadge() {
        return progressBadge;
    }

    public void goWithStandardWay(){
        gm.clickOnOneOfElements(getStartedBtns, 1);
    }

    public String getExistingEmail(){
        gm.waitForElementToBeVisible(emailField, 30);
        return emailField.getAttribute("value");
    }

    public void selectDOB (String year, String month, String day) {
        gm.clickOnElement(dobField);
        selectYear(year);
        selectMonth(month);
        selectDay(day);
        String dob = month + " " + day + ", " + year;
        System.out.println(dobField.getAttribute("value"));
        Assert.assertEquals("Correct dob should be displayed", dob, dobField.getAttribute("value"));
    }

    private void selectYear (String year) {
        //select year
        gm.clickOnElement(monthYearBtn.get(1));
        gm.pause(1000);
        List<String> yearsList = new ArrayList<>();
        int index = 0;
        do {
            for (WebElement yearElement : years) {
                yearsList.add(yearElement.getText().trim());
            }
            System.out.println(yearsList);
            if (Integer.parseInt(yearsList.get(0)) > Integer.parseInt(year)) {
                changeYearView.get(0).click();
                yearsList.clear();
            }
            else if (Integer.parseInt(yearsList.get(yearsList.size() - 1)) < Integer.parseInt(year)) {
                changeYearView.get(1).click();
                yearsList.clear();
            }
            else index = yearsList.indexOf(year);
        } while (!yearsList.contains(year));
        years.get(index).click();
    }

    private void selectMonth (String month) {
        gm.clickOnElement(monthYearBtn.get(0));
        gm.pause(500);
        List<String> monthList = new ArrayList<>();
        for (WebElement monthElement : months) {
            monthList.add(monthElement.getText().trim());
        }
        int index = monthList.indexOf(month);
        months.get(index).click();
    }

    private void selectDay (String day) {
        List<String> dayList = new ArrayList<>();
        gm.pause(500);
        for (WebElement dayElement : dates) {
            dayList.add(dayElement.getText().trim());
        }
        int index = dayList.indexOf(day);
        dates.get(index).click();
    }

    public void fillInForm (String email, String year, String month, String day, String country, int gender, String progress) {
        gm.typeIn(emailField, email);
        selectDOB(year, month, day);
        gm.typeIn(countrySelect, country);
        gm.clickOnOneOfElements(listItems, 0);
        gm.clickOnElement(genderSelect);
        gm.clickOnOneOfElements(listItems, gender);
        Assert.assertEquals("Progress should be " + progress, progress, progressBadge.getText().trim());
    }

    public void successPath(String email, String validYear, String month, String day, String country) {
        goWithStandardWay();
        fillInForm(email, validYear, month, day, country, 0, "33%");
        gm.clickOnElement(getSubmitBtn());
    }
}
