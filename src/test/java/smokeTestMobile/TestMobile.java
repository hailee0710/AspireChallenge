package smokeTestMobile;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Set test run order with Name Ascending order
public class TestMobile extends smokeTestMobile.BaseTestMobile {
    //region Variables declaration
    //Get page object and helpers
    final static Logger logger = Logger.getLogger(TestMobile.class);
    //Get all test data
    private static String testEnvironment;
    private static String postcode;
    private static String searchKeyword;
    private static String findStorePage;
    private static String configuratorPage;
    private static String kitchenalityPage;
    private static String displaySales;
    private static String orderCatalogPage;
    private static String magazinePage;
    private static String flagshipPartnerPage;
    private static String galleryPage;
    private static String email;

    @BeforeClass
    public static void getAllTestData() {
        testEnvironment = commonMethods.getTestData(commonMethods.getProperties("environment"));
        postcode = commonMethods.getTestData("Postcode");
        searchKeyword = commonMethods.getTestData("SearchKeyword");
        findStorePage = commonMethods.getTestData("FindStorePage");
        configuratorPage = commonMethods.getTestData("ConfiguratorPage");
        kitchenalityPage = commonMethods.getTestData("KitchenalityPage");
        displaySales = commonMethods.getTestData("DisplaySalesPage");
        orderCatalogPage = commonMethods.getTestData("OrderCatalogPage");
        magazinePage = commonMethods.getTestData("MagazinePage");
        flagshipPartnerPage = commonMethods.getTestData("FlagshipPartnerPage");
        galleryPage = commonMethods.getTestData("GalleryPage");
        email = commonMethods.getProperties("Email");
    }
    //endregion


}
