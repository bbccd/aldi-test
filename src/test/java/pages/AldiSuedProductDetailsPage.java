package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

public class AldiSuedProductDetailsPage {
    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="www.aldi-sued.de/de";

    @FindBy(className = "container-fluid")
    private WebElement container;

    @FindBy(className = "target_product_name")
    private WebElement targetProductName;

    @FindBy(className = "store-checker-btn")
    private WebElement checkStoreAvailabilityButton;

    @FindBy(id = "storeAddressSearch")
    private WebElement storeSearchBoxByPostalCode;

    @FindBy(id = "search-stores-stock")
    private WebElement storeSearchSubmitButton;

    // @FindBy(className = "google-maps-cookie-enabled")
    // private WebElement acceptGooglePolicyButton;

    @FindBy(className = "google-maps-tooltip")
    private WebElement googleMapsTooltip;

    @FindBy(id = "dealer-list")
    private WebElement dealerList;

    //Constructor
    public AldiSuedProductDetailsPage (WebDriver driver){
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public Boolean checkStoreAvailability(String postalCode, int level) throws InterruptedException {
        //Instantiating Actions class
        Actions actions = new Actions(driver);
        //Hovering on Store Availability button
        actions.moveToElement(checkStoreAvailabilityButton).build().perform();
        Thread.sleep(1000);
        googleMapsTooltip.sendKeys(Keys.TAB);
        googleMapsTooltip.sendKeys(Keys.ENTER);

        WebElement acceptGooglePolicyButton = googleMapsTooltip.findElement(By.className("google-maps-cookie-enabled"));

        acceptGooglePolicyButton.click();
        //
        checkStoreAvailabilityButton.click();

        storeSearchBoxByPostalCode.clear();
        storeSearchBoxByPostalCode.sendKeys(postalCode);
        storeSearchSubmitButton.click();

        Thread.sleep(3000);
        WebElement dealersSubList = dealerList.findElement(By.tagName("ul"));
        List<WebElement> dealers = dealersSubList.findElements(By.tagName("li"));
        System.out.println("Number of dealers found: " + dealers.size());

        WebElement firstStore = dealerList.findElement(By.id("dealer-id-1"));
        WebElement firstStoreAvailability = firstStore.findElement(By.className("badge-dealer-stock"));
        int availabilityLevel = 0;
        if (firstStoreAvailability.getAttribute("class").contains("badge-danger")){
            availabilityLevel = 1;
        } else if (firstStoreAvailability.getAttribute("class").contains("badge-warning")){
            availabilityLevel = 2;
        } else if (firstStoreAvailability.getAttribute("class").contains("badge-success")) {
            availabilityLevel = 3;
        }
        System.out.println("Availability level at first dealer: " + availabilityLevel);

        // if user wants NO availability, check strictly that item is NOT available:
        if (level == 0) {
            if (availabilityLevel == 0) {
                return true;
            } else {
                return false;
            }
        }

        // else, check that availability is at least as good as requested:
        if (availabilityLevel >= level) {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean productNameAsserts(String productName) {
        System.out.println("Text on item found: " + targetProductName.getText());
        return targetProductName.getText().equals(productName);
    }
}
