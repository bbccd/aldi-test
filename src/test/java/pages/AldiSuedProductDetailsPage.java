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
        handleGoogleMapsPopover();

        checkStoreAvailabilityButton.click();
        fillOutAndSubmitStoreSearchUI(postalCode);

        Thread.sleep(10000);
        WebElement dealersSubList = dealerList.findElement(By.tagName("ul"));
        List<WebElement> dealers = dealersSubList.findElements(By.tagName("li"));
        System.out.println("Number of dealers found: " + dealers.size());

        WebElement firstStore = dealerList.findElement(By.id("dealer-id-1"));
        WebElement firstStoreAvailability = firstStore.findElement(By.className("badge-dealer-stock"));

        int availabilityLevel = getFirstStoreAvailability(firstStoreAvailability);

        Boolean returnValue = checkStoreAvailabilityAgainstThreshold(availabilityLevel, level);
        return returnValue;
    }

    public Boolean productNameAsserts(String productName) {
        System.out.println("Text on item found: " + targetProductName.getText());
        return targetProductName.getText().equals(productName);
    }

    private void fillOutAndSubmitStoreSearchUI(String postalCode){

        storeSearchBoxByPostalCode.clear();
        storeSearchBoxByPostalCode.sendKeys(postalCode);
        storeSearchSubmitButton.click();
    }

    private void handleGoogleMapsPopover() throws InterruptedException {
        // To make sure the popover displays, we simulate a mouse hover over the "Verfügbarkeit prüfen" button
            //Instantiating Actions class
            Actions actions = new Actions(driver);
            //Hovering on Store Availability button
            actions.moveToElement(checkStoreAvailabilityButton).build().perform();
            Thread.sleep(1000);
        // Try to close the popover (called "tooltip" internally) using keyboard buttons (doesn't work reliably, but doesn't hurt either):
        googleMapsTooltip.sendKeys(Keys.TAB);
        googleMapsTooltip.sendKeys(Keys.ENTER);
        // Again, try to  close by explicitly pushing the button (first need to find it now after hovering; this works more reliably):
        WebElement acceptGooglePolicyButton = googleMapsTooltip.findElement(By.className("google-maps-cookie-enabled"));
        acceptGooglePolicyButton.click();
    }

    private int getFirstStoreAvailability(WebElement firstStoreAvailability) {
        int availabilityLevel = 0;
        if (firstStoreAvailability.getAttribute("class").contains("badge-danger")){
            availabilityLevel = 1;
        } else if (firstStoreAvailability.getAttribute("class").contains("badge-warning")){
            availabilityLevel = 2;
        } else if (firstStoreAvailability.getAttribute("class").contains("badge-success")) {
            availabilityLevel = 3;
        }
        System.out.println("Availability level at first dealer: " + availabilityLevel);
        return availabilityLevel;
    }

    private Boolean checkStoreAvailabilityAgainstThreshold(int actualAvailabilityLevel, int thresholdAvailabilityLevel){
        System.out.println("Checking availability level. Actual value: " + actualAvailabilityLevel + ", threshold value: " + thresholdAvailabilityLevel);
        // if user wants NO availability, check strictly that item is NOT available:
        if (thresholdAvailabilityLevel == 0) {
            if (actualAvailabilityLevel == 0) {
                System.out.println("Availability should be 0 and is actually 0. Returning true.");
                return true;
            } else {
                System.out.println("Availability should be 0, but is actually " + actualAvailabilityLevel + ". Returning false.");
                return false;
            }
        }

        // else, check that availability is at least as good as requested:
        if (actualAvailabilityLevel >= thresholdAvailabilityLevel) {
            System.out.println("Availability should be " + thresholdAvailabilityLevel + " or higher, and is actually " + actualAvailabilityLevel + ". Returning true.");
            return true;
        } else {
            System.out.println("Availability should be " + thresholdAvailabilityLevel + " or higher, and is actually " + actualAvailabilityLevel + ". Returning false.");
            return false;
        }
    }
}
