package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AldiSuedHomePage {
    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="https://www.aldi-sued.de/de";

    @FindBy(id = "c-modal")
    private WebElement cookieModalDialog;

    @FindBy(xpath = "/html/body/div[2]/div/div/form/div[3]/div/div[1]/button")
    private WebElement confirmCookieSelectionModal;

    @FindBy(id = "input_search")
    private WebElement searchBar;

    @FindBy(xpath = "/html/body/header/div[6]/div[1]/div[1]/div/div[1]/form/div/button")
    private WebElement submitSearchButton;

    //Constructor
    public AldiSuedHomePage (WebDriver driver){
        this.driver = driver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    //We will use this boolean for assertion. To check if page is opened
    public boolean isModalDialogDisplayed(){
        return cookieModalDialog.isDisplayed();
    }

    public void closeModalDialog(){
        confirmCookieSelectionModal.click();
    }

    public AldiSuedSearchResultsPage submitSearch(String searchstring){
        searchBar.clear();
        searchBar.sendKeys(searchstring);
        submitSearchButton.click();
        return PageFactory.initElements(driver,
                AldiSuedSearchResultsPage.class);
    }

}