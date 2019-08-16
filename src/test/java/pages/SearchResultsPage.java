package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sadiq on 23/07/19.
 */
public class SearchResultsPage extends BasePage {

    private WaitUtil waitUtil;
    private int explicitTimeOut;



    @FindBy(id = "fareTrendsDiv")
    WebElement fareTrends;

    @FindBy(id = "searchWrapOuterBox")
    WebElement searchBox;

    @FindBy(id = "footer")
    WebElement footer;

    @FindBy(id = "sortByPriceOnw")
    WebElement sortPrice;

    @FindBy(xpath = "//div[@class='cardContainer']//span[starts-with(@class,'fr')]//span[starts-with(@id,'o')]")
    List<WebElement> priceList;


    public SearchResultsPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
        waitUtil = new WaitUtil(driver);
        explicitTimeOut = waitUtil.getExplicitTimeout();

    }

    public void waitForFareTrendsDialogToBeVisible(){
        waitUtil.isElementVisible(fareTrends, explicitTimeOut);
    }

    public void scrollToBottomOfFlightSearchPage(WebDriver driver){


        int y = footer.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,"+y+")");

    }

    public void scrollToTopOfFlightSearchPage(WebDriver driver){
        int y = searchBox.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,"+y+")");
    }

    public void verifyPriceFilterExists(){
        waitUtil.isElementVisible(sortPrice, explicitTimeOut);
    }

    public void clickOnPriceFilter(){
        sortPrice.click();
    }

    public void getCostOfAllFlightsSortedFromHighToLow(){
        List<Integer> priceListInDescendingOrder  = new LinkedList<>();



        for(WebElement costOfEachFlight : priceList){
            priceListInDescendingOrder.add(Integer.parseInt(costOfEachFlight.getText().replaceAll(",","")));
        }

        verifyPriceSortedFromHighToLow(priceListInDescendingOrder);
    }

    public Boolean verifyPriceSortedFromHighToLow(List<Integer> comparePriceList){

        int currentMaxValue = comparePriceList.get(0);

        for(int i =1; i<comparePriceList.size(); i++){

            if(currentMaxValue < comparePriceList.get(i)) {
                System.out.println("The list is not sorted in High to low");
                return false;

            }
            currentMaxValue = comparePriceList.get(i);
        }
        System.out.println("The list is Sorted in High to low");
        return true;
    }







}
