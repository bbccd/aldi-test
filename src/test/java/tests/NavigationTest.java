package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pages.AldiSuedHomePage;
import pages.AldiSuedProductDetailsPage;
import pages.AldiSuedSearchResultsPage;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class NavigationTest {

    WebDriver driver;

    String productName = "AMAZFIT Smarte Waage mit App-Funktion";
    String searchTerm = productName; // "490000000000714131";

    String postalCode = "53343"; // "53343";
    int availabilityLevel = 2; // meaning: 0 = not available, 1 = low stock, 2 = available

    @BeforeMethod
    public void beforeMethod() {
        // set path of Chromedriver executable
        System.setProperty("webdriver.chrome.driver",
                "./src/test/resources/drivers/chromedriver");

        // initialize new WebDriver session
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
    }

    @Test
    public void searchAndCheckAvailabilityTest() {
        AldiSuedHomePage aldiSuedHomePage = new AldiSuedHomePage(driver);
        if ( aldiSuedHomePage.isModalDialogDisplayed() ) {
            aldiSuedHomePage.closeModalDialog();
        }
        AldiSuedSearchResultsPage aldiSearchResultPage = aldiSuedHomePage.submitSearch(searchTerm);
        // Validate page title
        Assert.assertEquals(driver.getTitle(), "Suchergebnis");
        try {
            createScreenshot("resultspage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        AldiSuedProductDetailsPage detailsPage = aldiSearchResultPage.openResult();
        Assert.assertTrue(detailsPage.productNameAsserts(productName));
        try {
            createScreenshot("detailspage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check availability (assert that the availability in the first search result
        //    for a store is at least of the availabilty level defined;
        //    stores are searched by postal code, and are listed in order of proximity):
        try {
            Assert.assertTrue(detailsPage.checkStoreAvailability(postalCode, availabilityLevel));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createScreenshot(String fileName) throws IOException {
        String filePath = "./target/" + fileName;
        // Create screenshot:
        File scrFile  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(filePath));
    }

}