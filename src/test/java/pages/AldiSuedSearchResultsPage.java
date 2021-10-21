package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class AldiSuedSearchResultsPage {

    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="www.aldi-sued.de/de/suchergebnis.html";

    @FindBy(className = "list-of-products")
    private WebElement resultsList;


    //Constructor
    public AldiSuedSearchResultsPage (WebDriver driver){
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    // Open the details page to the first result item in the list of search result items.
    public AldiSuedProductDetailsPage openResult(){
        // Click the Details button on the correct result
        // list results = resultsList.findElements(By.className("plp_product"));
        WebElement moreInfoButton = resultsList.findElement(By.className("plp-more-info-btn"));
        moreInfoButton.click();
        return PageFactory.initElements(driver,
                AldiSuedProductDetailsPage.class);
    }

}
