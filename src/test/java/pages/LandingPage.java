package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtil;

import java.util.List;

/**
 * Created by sadiq on 23/07/19.
 */
public class LandingPage extends BasePage{

    private WaitUtil waitUtil;
    private int explicitTimeOut;

    @FindBy(id = "gosuggest_inputSrc")
    WebElement sourceLocation;

    @FindBy(xpath = "//ul[@role='listbox']//li")
    List<WebElement> sourceLocationSuggestions;

    @FindBy(id = "gosuggest_inputDest")
    WebElement destination;

    @FindBy(xpath = "//ul[@role='listbox']//li")
    List<WebElement> destinationSuggestions;


    @FindBy(id = "gi_search_btn")
     WebElement searchButton;

    public WebElement getSearchButton() {
        return searchButton;
    }



    public LandingPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver,this);
        waitUtil = new WaitUtil(driver);
        explicitTimeOut = waitUtil.getExplicitTimeout();
    }

    public void enterSourceLocation(String source){
        sourceLocation.clear();
        sourceLocation.sendKeys(source);
    }

    public void waitForSourceSuggesstionsToBeDisplayed(){
        waitUtil.isElementVisible(sourceLocationSuggestions.get(0), explicitTimeOut);
    }

    public void selectFirstSuggestionFromSourceLocationSuggesstions(){
        sourceLocationSuggestions.get(0).click();
    }


    public void enterDestinationLocation(String destinationLocation){
        destination.clear();
        destination.sendKeys(destinationLocation);
    }

    public void waitForDestinationSuggesstionsToBeDisplayed(){
        waitUtil.isElementVisible(destinationSuggestions.get(0), explicitTimeOut);
    }

    public void selectFirstSuggestionFromDestinationSuggesstions(){
        destinationSuggestions.get(0).click();
    }

    public void searchFlights(){
        getSearchButton().click();
    }




}
