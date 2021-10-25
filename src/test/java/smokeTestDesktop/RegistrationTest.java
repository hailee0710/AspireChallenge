package smokeTestDesktop;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.time.Instant;
import java.time.Year;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Set test run order with Name Ascending order
public class RegistrationTest extends BaseTestDesktop {
    //region Variables declaration
    //Get page object and helpers
    final static Logger logger = Logger.getLogger(RegistrationTest.class);
    //Get all test data
    private static String loginLink;
    private static String validUserName;
    private static String validPreferredName;
    private static String validEmail;
    private static String validHeardAbout;
    private static String invalidReferalCode;
    private static String usernameWithSpecialChar;
    private static String usernameWithNumber;
    private static String existingPhone;
    private static String existingEmail;
    private static String invalidEmail;
    private static String emailErrorMsg;
    private static String existingPhoneMsg;
    private static String existingEmailMsg;
    private static String phoneFormatErrorMsg;
    private static String referalCodeErrorMsg;
    private static String emptyPhoneErrorMsg;
    private static String longPhoneNumber;
    private static String shortPhoneNumber;
    private static String longPhoneErrorMsg;
    private static String validOTP;
    private static String invalidOTP;
    private static String invalidOtpErrMsg;
    private static String resendMsg;
    private static String inviteDirectorMsg;
    private static String notRegisteredMsg;
    private static String validYear;
    private static String futureYear;
    private static String month;
    private static String day;
    private static String tooYoungErrorMsg;
    private static String emptyEmailErrorMsg;
    private static String country;
    private static String willBeThereMsg;
    private static String businessLegalName;
    private static String invalidUEN;
    private static String existingUEN;
    private static String UENErrorMsg;
    private static String existingUENErrorMsg;
    private static String websiteURL;
    private static String shortBAErrorMsg;
    private static String shortDetailsErrorMsg;
    private static String websiteUrlErrorMsg;
    private static String documentPath;
    private static String imagePath;
    private static String mainAddress;
    private static String city;
    private static String postcode;
    private static String province;

    @BeforeClass
    public static void getAllTestData() {
        loginLink = commonMethods.getTestData("LoginPage");
        validUserName = commonMethods.getTestData("ValidUserName");
        validPreferredName = commonMethods.getTestData("ValidPreferredName");
        validEmail = commonMethods.getTestData("ValidEmail");
        validHeardAbout = commonMethods.getTestData("ValidHeardAbout");
        invalidReferalCode = commonMethods.getTestData("InvalidReferalCode");
        usernameWithSpecialChar = commonMethods.getTestData("UsernameWithSpecialChar");
        usernameWithNumber = commonMethods.getTestData("UsernameWithNumber");
        existingPhone = commonMethods.getTestData("ExistingPhone");
        existingEmail = commonMethods.getTestData("ExistingEmail");
        invalidEmail = commonMethods.getTestData("InvalidEmail");
        emailErrorMsg = commonMethods.getTestData("EmailErrorMsg");
        existingPhoneMsg = commonMethods.getTestData("ExistingPhoneMsg");
        existingEmailMsg = commonMethods.getTestData("ExistingEmailMsg");
        phoneFormatErrorMsg = commonMethods.getTestData("PhoneFormatErrorMsg");
        referalCodeErrorMsg = commonMethods.getTestData("ReferalCodeErrorMsg");
        emptyPhoneErrorMsg = commonMethods.getTestData("EmptyPhoneErrorMsg");
        longPhoneNumber = commonMethods.getTestData("LongPhoneNumber");
        shortPhoneNumber = commonMethods.getTestData("ShortPhoneNumber");
        longPhoneErrorMsg = commonMethods.getTestData("LongPhoneErrorMsg");
        validOTP = commonMethods.getTestData("ValidOTP");
        invalidOTP = commonMethods.getTestData("InvalidOTP");
        invalidOtpErrMsg = commonMethods.getTestData("InvalidOtpErrMsg");
        resendMsg = commonMethods.getTestData("ResendMsg");
        inviteDirectorMsg = commonMethods.getTestData("InviteDirectorMsg");
        notRegisteredMsg = commonMethods.getTestData("NotRegisteredMsg");
        month = commonMethods.getTestData("Month");
        day = commonMethods.getTestData("Day");
        validYear = commonMethods.getTestData("ValidYear");
        futureYear = commonMethods.getTestData("FutureYear");
        tooYoungErrorMsg = commonMethods.getTestData("TooYoungErrorMsg");
        emptyEmailErrorMsg = commonMethods.getTestData(("EmptyEmailErrorMsg"));
        country = commonMethods.getTestData("Country");
        willBeThereMsg = commonMethods.getTestData("WillBeThereMsg");
        businessLegalName = commonMethods.getTestData("BusinessLegalName");
        invalidUEN = commonMethods.getTestData("InvalidUEN");
        existingUEN = commonMethods.getTestData("ExistingUEN");
        UENErrorMsg = commonMethods.getTestData("UENErrorMsg");
        existingUENErrorMsg = commonMethods.getTestData("ExistingUENErrorMsg");
        websiteURL = commonMethods.getTestData("WebsiteURL");
        shortBAErrorMsg = commonMethods.getTestData("ShortBAErrorMsg");
        shortDetailsErrorMsg = commonMethods.getTestData("ShortDetailsErrorMsg");
        websiteUrlErrorMsg = commonMethods.getTestData("WebsiteUrlErrorMsg");
        documentPath = commonMethods.getTestData("DocumentPath");
        imagePath = commonMethods.getTestData("ImagePath");
        mainAddress = commonMethods.getTestData("MainAddress");
        city = commonMethods.getTestData("City");
        postcode = commonMethods.getTestData("Postcode");
        province = commonMethods.getTestData("Province");
    }
    //endregion

    @Test
    public void registerWithInvalidEmail() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid Email======");
        String phone = String.valueOf(Instant.now().getEpochSecond());

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);
        generalMethods.compareImagesSB(folderName, "Register_page", null);

        generalMethods.printTestStep("2.Enter all valid information but email");
        registrationPage.fillInForm(validUserName, validPreferredName, invalidEmail, phone, 0, 1,  null, true);
        //Verify error message shows up on email field
        String errorMsg = registrationPage.getEmailErrorMsg();
        Assert.assertEquals("Error message should be displayed on Email field",emailErrorMsg, errorMsg);
    }

    @Test
    public void registerWithExistingPhone() throws Exception {
        generalMethods.printTestName("======Start testing Register with Existing Phone======");
        String email = String.format(validEmail, String.valueOf(Instant.now().getEpochSecond()));

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing phone number");
        registrationPage.fillInForm(validUserName, validPreferredName, email, existingPhone, 1, 1,  null, true);
        registrationPage.goNext();
        //Verify error message shows up on phone field
        registrationPage.validateErrorMsg(existingPhoneMsg, null);

        generalMethods.printTestStep("3.Click on Login and verify that OTP page with entered phone number is displayed");
        generalMethods.clickOnElement(registrationPage.getLoginBtn());
        String phone = otpPage.getPhoneOrEmail().replace(" ", "");
        Assert.assertTrue("Phone number must be the same with the entered phone number", phone.contains(existingPhone));
    }

    @Test
    public void registerWithExistingEmail() throws Exception {
        generalMethods.printTestName("======Start testing Register with Existing Email======");
        String phone = String.valueOf(Instant.now().getEpochSecond());

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing email");
        registrationPage.fillInForm(validUserName, validPreferredName, existingEmail, phone, 1, 1,  null, true);
        registrationPage.goNext();
        //Verify error message shows up on email field
        registrationPage.validateErrorMsg(null, existingEmailMsg);

        generalMethods.printTestStep("3.Click on Login and verify that OTP page with entered email is displayed");
        generalMethods.clickOnElement(registrationPage.getLoginBtn());
        String email = otpPage.getPhoneOrEmail();
        Assert.assertEquals("Email must be the same with the entered Email", existingEmail, email);
    }

    @Test
    public void registerWithExistingEmailAndPhone() throws Exception {
        generalMethods.printTestName("======Start testing Register with Existing Email and Phone======");

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing email");
        registrationPage.fillInForm(validUserName, validPreferredName, existingEmail, existingPhone, 1, 1,  null, true);
        registrationPage.goNext();
        //Verify error message shows up on phone and email field
        registrationPage.validateErrorMsg(existingPhoneMsg, existingEmailMsg);

        generalMethods.printTestStep("3.Click on Login and verify that OTP page with entered email is displayed");
        generalMethods.clickOnElement(registrationPage.getLoginBtn());
        String email = otpPage.getPhoneOrEmail();
        Assert.assertEquals("Email must be the same with the entered Email", existingEmail, email);
    }

    @Test
    public void registerWithInvalidPhone() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid Phone======");
        String phone = generalMethods.getRandomString(9);
        String email = String.format(validEmail, String.valueOf(Instant.now().getEpochSecond()));

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing email");
        registrationPage.fillInForm(validUserName, validPreferredName, email, phone, 1, 1,  null, true);
        registrationPage.goNext();
        //Verify error message shows up on phone field
        registrationPage.validateErrorMsg(phoneFormatErrorMsg, null);
    }

    @Test
    public void registerWithLongPhone() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid Phone======");
        String email = String.format(validEmail, String.valueOf(Instant.now().getEpochSecond()));

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing email");
        registrationPage.fillInForm(validUserName, validPreferredName, email, longPhoneNumber, 1, 1,  null, true);
        //Verify error message shows up on phone field
        registrationPage.validateErrorMsg(longPhoneErrorMsg, null);
    }

    @Test
    public void registerWithShortPhone() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid Phone======");
        String email = String.format(validEmail, String.valueOf(Instant.now().getEpochSecond()));

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but Existing email");
        registrationPage.fillInForm(validUserName, validPreferredName, email, shortPhoneNumber, 1, 1,  null, true);
        //Verify error message shows up on phone field
        registrationPage.validateErrorMsg(emptyPhoneErrorMsg, null);
    }

//    @Test
//    public void registerWithLongName() throws Exception {
//        generalMethods.printTestName("======Start testing Register with Long Name======");
//        String phone = String.valueOf(Instant.now().getEpochSecond());
//        String name = generalMethods.getRandomString(1000);
//        String email = String.format(validEmail, phone);
//
//        generalMethods.printTestStep("1.User opens the Login page");
//        generalMethods.openURL(loginLink, 120);
//        loginPage.goToRegisterPage();
//        generalMethods.waitForElementToBeVisible(registrationPage.fullNameField, 30);
//
//        generalMethods.printTestStep("2.Enter all valid information but email");
//        registrationPage.fillInForm(name, validPreferredName, validEmail, phone, 0, 1,  null, true);
//        registrationPage.goNext();
//        //Verify error message shows up on email field
//        generalMethods.waitInSeconds(30);
//        String errorMsg = registrationPage.getEmailErrorMsg();
//        Assert.assertEquals(emailErrorMsg, errorMsg);
//    }

    @Test
    public void registerWithInvalidReferalCode() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid Referal Code======");
        String phone = String.valueOf(Instant.now().getEpochSecond());
        String email = String.format(validEmail, phone);

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Enter all valid information but referal code");
        registrationPage.fillInForm(validUserName, validPreferredName, email, phone, 0, 1,  invalidReferalCode, true);
        //Verify error message shows up on email field
        String errorMsg = registrationPage.getReferalErrorMsg();
        Assert.assertEquals(referalCodeErrorMsg, errorMsg);
    }

    @Test
    public void registerWithEmptyFields() throws Exception {
        generalMethods.printTestName("======Start testing Register with Empty Fields======");
        String phone = String.valueOf(Instant.now().getEpochSecond());
        String email = String.format(validEmail, phone);

        generalMethods.printTestStep("1.User opens the Login page");
        loginPage.goToRegisterPage(loginLink);

        generalMethods.printTestStep("2.Leave required fields empty and check the state of the Continue Button");
        //Leave all fields empty
        registrationPage.fillInForm("", "", "", null, 0, 1,  null, true);
        String state = registrationPage.getButtonState();
        Assert.assertTrue("The Continue button should be disabled",state.contains("disabled"));

        //Leave all fields empty but fullname
        registrationPage.fillInForm(validUserName, "", "", null, -1, -1,  null, true);
        state = registrationPage.getButtonState();
        Assert.assertTrue("The Continue button should be disabled",state.contains("disabled"));

        //Leave all fields empty but fullname and Email
        registrationPage.fillInForm(validUserName, "", email, "", -1, -1,  null, true);
        state = registrationPage.getButtonState();
        Assert.assertTrue("The Continue button should be disabled",state.contains("disabled"));

        //Verify error message shows up on phone field
        registrationPage.validateErrorMsg(emptyPhoneErrorMsg, null);
        //Leave all fields empty but fullname, email and phone
        registrationPage.fillInForm(validUserName, "", email, phone, -1, -1,  null, true);
        state = registrationPage.getButtonState();
        Assert.assertTrue("The Continue button should be disabled",state.contains("disabled"));

        //Uncheck the terms box
        registrationPage.fillInForm(validUserName, "", email, phone, -1, -1,  null, false);
        state = registrationPage.getButtonState();
        Assert.assertTrue("The Continue button should be disabled",state.contains("disabled"));

        //Check the terms box
        registrationPage.fillInForm(validUserName, "", email, phone, -1, -1,  null, true);
        state = registrationPage.getButtonState();
        Assert.assertFalse("The Continue button should be disabled", state.contains("disabled"));

    }

    @Test
    public void registerWithInvalidOTP() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid OTP======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);

        generalMethods.printTestStep("1. Enter Invalid OTP");
        otpPage.enterOTP(invalidOTP);
        otpPage.validateErrorMsg(invalidOtpErrMsg);
    }

    @Test
    public void checkResendOTP() throws Exception {
        generalMethods.printTestName("======Start testing Check Resend OTP======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        generalMethods.compareImagesSB(folderName, "OTP_page", null);

        generalMethods.printTestStep("1. Check resend function");
        otpPage.resendOTP(resendMsg);
    }

    @Test
    public void checkDirectorRoleNotRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Director Role======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Director role");
        businessRolePage.directorSelect();
        businessRolePage.checkNotRegister();
        generalMethods.printTestStep("2. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("Invite Message should be displayed", notRegisteredMsg, businessRolePage.getScreenTitle());
    }

    @Test
    public void checkDirectorRoleRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Director Role======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Director role");
        businessRolePage.directorSelect();
        businessRolePage.selectASolution(0);
        generalMethods.printTestStep("2. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("There should be 2 Get Started buttons", 2, personalDetailsPage.getGetStartedBtns().size());
    }

    @Test
    public void checkEmployeeRoleNotRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Employee Role======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Employee role");
        businessRolePage.employeeSelect();
        businessRolePage.checkNotRegister();
        generalMethods.printTestStep("2. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("Invite Message should be displayed", notRegisteredMsg, businessRolePage.getScreenTitle());

    }

    @Test
    public void checkEmployeeRoleRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Employee Role======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Employee role");
        businessRolePage.employeeSelect();
        businessRolePage.selectASolution(0);
        generalMethods.printTestStep("2. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("Invite Message should be displayed", inviteDirectorMsg, businessRolePage.getScreenTitle());

    }

    @Test
    public void checkFreelancerRole() throws Exception {
        generalMethods.printTestName("======Start testing Check Freelancer Role======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Freelancer role");
        businessRolePage.freelancerSelect();
        businessRolePage.checkFreelancerOptions();
    }

    @Test
    public void checkFreelancerNotRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Freelancer Not registered======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Freelancer role");
        businessRolePage.freelancerSelect();
        generalMethods.printTestStep("2. No, I don't in second question");
        businessRolePage.selectASolution(0);
        generalMethods.clickOnOneOfElements(businessRolePage.getRegisteredBusiness(), 1);
        generalMethods.typeIn(businessRolePage.getCountrySelect(), country);
        //businessRolePage.getListItems().get(0).click();
        generalMethods.printTestStep("3. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("Invite Message should be displayed", willBeThereMsg, businessRolePage.getScreenTitle());
    }

    @Test
    public void checkFreelancerRegistered() throws Exception {
        generalMethods.printTestName("======Start testing Check Freelancer Not registered======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Freelancer role");
        businessRolePage.freelancerSelect();
        generalMethods.printTestStep("2. No, I don't in second question");
        businessRolePage.selectASolution(0);
        generalMethods.clickOnOneOfElements(businessRolePage.getRegisteredBusiness(), 0);
        generalMethods.typeIn(businessRolePage.getCountrySelect(), country);
        businessRolePage.getListItems().get(0).click();
        generalMethods.printTestStep("3. Click on Continue button");
        businessRolePage.goNext();
        Assert.assertEquals("There should be 2 Get Started buttons", 2, personalDetailsPage.getGetStartedBtns().size());
    }

    @Test
    public void checkSelectAllSolutions() throws Exception {
        generalMethods.printTestName("======Start testing Check All Solutions======");
        loginPage.goToRegisterPage(loginLink);
        registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();

        generalMethods.printTestStep("1. Select Director role");
        businessRolePage.directorSelect();
        businessRolePage.selectAllSolutions();
        businessRolePage.getBackBtn().click();

        generalMethods.printTestStep("2. Select Employee role");
        businessRolePage.employeeSelect();
        businessRolePage.selectAllSolutions();
        businessRolePage.getBackBtn().click();

        generalMethods.printTestStep("3. Select Freelancer role");
        businessRolePage.freelancerSelect();
        businessRolePage.selectAllSolutions();
    }

    @Test
    public void checkPersonalDetailsWithTooYoungAge() {
        generalMethods.printTestName("======Start testing Check Director Personal Details with Too Young age======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();
        Assert.assertEquals("Correct email should be displayed", email, personalDetailsPage.getExistingEmail());
        generalMethods.printTestStep("1. Select DOB to be less than 18 years from now");
        String year = String.valueOf(Year.now().getValue() - 5);
        personalDetailsPage.selectDOB(year, month, day);
        String errorMsg = personalDetailsPage.getDobErrorMsg().getText();
        Assert.assertEquals("Correct error message shows up", tooYoungErrorMsg, errorMsg);
    }

    @Test
    public void checkPersonalDetailsWithFutureYear() {
        generalMethods.printTestName("======Start testing Check Director Personal Details with Future year======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();
        Assert.assertEquals("Correct email should be displayed", email, personalDetailsPage.getExistingEmail());
        generalMethods.printTestStep("1. Select DOB to be less than 18 years from now");
        String year = String.valueOf(Year.now().getValue() + 100);
        personalDetailsPage.selectDOB(year, month, day);
        String errorMsg = personalDetailsPage.getDobErrorMsg().getText();
        Assert.assertEquals("Correct error message shows up", tooYoungErrorMsg, errorMsg);
    }

    @Test
    public void checkPersonalDetailsWithExistingEmail() {
        generalMethods.printTestName("======Start testing Check Director Personal Details with Existing email======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();
        Assert.assertEquals("Correct email should be displayed", email, personalDetailsPage.getExistingEmail());
        generalMethods.printTestStep("1. Enter an existing email address to email field");
        personalDetailsPage.fillInForm(existingEmail, validYear, month, day, country, 0, "33%");
        generalMethods.clickOnElement(personalDetailsPage.getSubmitBtn());
        String errorMsg = personalDetailsPage.getEmailErrorMsg().getText();
        Assert.assertEquals("Correct error message shows up", existingEmailMsg, errorMsg);
    }

    @Test
    public void checkPersonalDetailsWithInvalidEmail() {
        generalMethods.printTestName("======Start testing Check Director Personal Details with Invalid email======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();
        Assert.assertEquals("Correct email should be displayed", email, personalDetailsPage.getExistingEmail());
        generalMethods.printTestStep("1. Enter an invalid email address to email field");
        personalDetailsPage.fillInForm(invalidEmail, validYear, month, day, country, 0, "26%");
        String errorMsg = personalDetailsPage.getEmailErrorMsg().getText();
        Assert.assertEquals("Correct error message shows up", emailErrorMsg, errorMsg);
    }

    @Test
    public void checkInvalidEmailOTP() throws Exception {
        generalMethods.printTestName("======Start testing Register with Invalid OTP======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();

        generalMethods.printTestStep("1. Enter all valid information");
        personalDetailsPage.fillInForm(email, validYear, month, day, country, 0, "33%");
        generalMethods.clickOnElement(personalDetailsPage.getSubmitBtn());
        generalMethods.printTestStep("2. Enter Invalid OTP");
        otpPage.enterOTP(invalidOTP);
        otpPage.validateErrorMsg(invalidOtpErrMsg);
    }

    @Test
    public void checkResendEmailOTP() throws Exception {
        generalMethods.printTestName("======Start testing Check Resend OTP======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.goWithStandardWay();

        generalMethods.printTestStep("1. Enter all valid information");
        personalDetailsPage.fillInForm(email, validYear, month, day, country, 0, "33%");
        generalMethods.clickOnElement(personalDetailsPage.getSubmitBtn());
        generalMethods.printTestStep("2. Check resend function");
        otpPage.resendOTP(resendMsg);
    }

    @Test
    public void checkBusinessDetailsWithInvalidUEN() {
        generalMethods.printTestName("======Start testing Register with Invalid UEN======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter Invalid UEN");
        businessDetailsPage.fillIn1stForm(businessLegalName, 0, 0, invalidUEN, 0, 0);
        businessDetailsPage.validateUENErrorMsg(UENErrorMsg);

    }

    @Test
    public void checkBusinessDetailsWithExistingUEN() {
        generalMethods.printTestName("======Start testing Register with Existing UEN======");
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter existing UEN");
        businessDetailsPage.fillIn1stForm(businessLegalName, 1, 1, existingUEN, 1, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.validateUENErrorMsg(existingUENErrorMsg);

    }

    @Test
    public void checkBusinessDetailsShortTextInFields() {
        generalMethods.printTestName("======Start testing Register with short text in fields======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String shortText = generalMethods.getRandomString(10);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(shortText, shortText, websiteURL, true,1, 1, 1);
        businessDetailsPage.validate3ErrorMsg(shortBAErrorMsg, shortDetailsErrorMsg, null);
    }

    @Test
    public void checkBusinessDetailsWithInvalidURL() {
        generalMethods.printTestName("======Start testing Register with Invalid URL======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, text, true,1, 1, 1);
        businessDetailsPage.goNext();
        businessDetailsPage.validate3ErrorMsg(null, null, websiteUrlErrorMsg);
    }

    @Test
    public void checkBusinessDetailsPEP() throws IOException {
        generalMethods.printTestName("======Start testing PEP page======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, websiteURL, true,1, 1, 1);
        businessDetailsPage.goNext();
        generalMethods.clickWithJavaExecuter(businessDetailsPage.getPEPicon());
        Assert.assertTrue("PEP page title should be displayed", businessDetailsPage.getPEPtitle().isDisplayed());
        generalMethods.compareImagesSB(folderName, "PEP_page", null);
    }

    @Test
    public void checkBusinessDetailsWithShareholders() throws IOException {
        generalMethods.printTestName("======Start testing Register with Invalid URL======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, websiteURL, false,1, 1, 1);
        businessDetailsPage.goNext();
        businessDetailsPage.select3rdForm(0, 2);
        businessDetailsPage.goNext();
        businessDetailsPage.uploadCertificate(System.getProperty("user.dir") + documentPath);
        Assert.assertFalse("Submit button should be enabled", businessDetailsPage.getContinueBtn().getAttribute("class").contains("disabled"));
    }

    @Test
    public void checkBusinessDetailsWithoutShareholders() throws IOException {
        generalMethods.printTestName("======Start testing Register without selecting shareholder======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, websiteURL, false,1, 1, 1);
        businessDetailsPage.goNext();
        businessDetailsPage.select3rdForm(1, 3);
        businessDetailsPage.goNext();
        businessDetailsPage.selectAllOptions(businessDetailsPage.getAccountUseSelect());
        businessDetailsPage.selectAllOptions(businessDetailsPage.getBusinessFundedSelect());
    }

    @Test
    public void checkBusinessDetailsWithSameAddress() throws IOException {
        generalMethods.printTestName("======Start testing Register without selecting shareholder======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, websiteURL, false,1, 1, 1);
        businessDetailsPage.goNext();
        businessDetailsPage.select3rdForm(1, 3);
        businessDetailsPage.goNext();
        businessDetailsPage.selectAnOption(businessDetailsPage.getAccountUseSelect(), 0);
        businessDetailsPage.selectAnOption(businessDetailsPage.getBusinessFundedSelect(), 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillInLastForm(true);
        businessDetailsPage.goNext();
        Assert.assertTrue("Identity details screen should be displayed", identityVerificationPage.getDocumentUpload().isDisplayed());
    }

    @Test
    public void checkBusinessDetailsWithDiffAddress() throws IOException {
        generalMethods.printTestName("======Start testing Register without selecting shareholder======");
        String uen = String.valueOf(Instant.now().getEpochSecond()).substring(0,9) + generalMethods.getRandomString(1);
        String text = generalMethods.getRandomString(40);
        loginPage.goToRegisterPage(loginLink);
        String email = registrationPage.validRegister(validEmail, validUserName);
        otpPage.enterOTP(validOTP);
        otpPage.goNext();
        businessRolePage.successPath();
        personalDetailsPage.successPath(email, validYear, month, day, country);
        otpPage.enterOTP(validOTP);

        generalMethods.printTestStep("1. Enter all information");
        businessDetailsPage.fillIn1stForm(businessLegalName, 2, 1, uen, 2, 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillIn2ndForm(text, text, websiteURL, false,1, 1, 1);
        businessDetailsPage.goNext();
        businessDetailsPage.select3rdForm(1, 3);
        businessDetailsPage.goNext();
        businessDetailsPage.selectAnOption(businessDetailsPage.getAccountUseSelect(), 0);
        businessDetailsPage.selectAnOption(businessDetailsPage.getBusinessFundedSelect(), 0);
        businessDetailsPage.goNext();
        businessDetailsPage.fillInLastForm(false, mainAddress, city, province, postcode);
        businessDetailsPage.goNext();
        Assert.assertTrue("Identity details screen should be displayed", identityVerificationPage.getDocumentUpload().isDisplayed());
    }



    
}
