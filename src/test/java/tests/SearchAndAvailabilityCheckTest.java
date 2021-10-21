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

public class SearchAndAvailabilityCheckTest {

    WebDriver driver;

    // Set up test parameters
    String productName = "AMAZFIT Smarte Waage mit App-Funktion";
    String searchTerm = productName; // "490000000000714131";
    String postalCode = "53343"; // "53343";
    int availabilityLevel = 1; // meaning: 0 = not available, 1 = very low stock, 2 = low stock, 3 = available

    @BeforeMethod
    public void beforeMethod() {
        // set path of Chromedriver executable
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver");

        // initialize new WebDriver session
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
    }

    @Test
    public void searchAndCheckAvailabilityTest() {
        // 1. open Aldi Süd home page, and close modal Cookies dialog if it is displayed:
        AldiSuedHomePage aldiSuedHomePage = new AldiSuedHomePage(driver);
        if ( aldiSuedHomePage.isModalDialogDisplayed() ) {
            aldiSuedHomePage.closeModalDialog();
        }
        // 2. search for the item on the home page, and submit search (navigates to search results page):
        AldiSuedSearchResultsPage aldiSearchResultPage = aldiSuedHomePage.submitSearch(searchTerm);

        // 3. Validate page title of search results page, and create a screen shot:
        Assert.assertEquals(driver.getTitle(), "Suchergebnis");
        try {
            createScreenshot("resultspage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. open the details page to the first search result:
        // TODO:  assert first that we have at least one search result before proceeding
        AldiSuedProductDetailsPage detailsPage = aldiSearchResultPage.openResult();

        // 5. Assert that the details page contains the product name, and create a screen shot:
        Assert.assertTrue(detailsPage.productNameAsserts(productName));
        try {
            createScreenshot("detailspage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 6. Check availability of item in store
        // (assert that the availability in the first search result
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