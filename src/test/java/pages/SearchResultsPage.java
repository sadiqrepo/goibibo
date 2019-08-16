package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.HelperUtil;
import utils.LogUtil;
import utils.WaitUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/** Created by sadiq on 23/07/19. */
public class SearchResultsPage extends BasePage {

  @FindBy(id = "fareTrendsDiv")
  WebElement fareTrends;

  @FindBy(id = "searchWrapOuterBox")
  WebElement searchBox;

  @FindBy(id = "footer")
  WebElement footer;

  @FindBy(xpath = "//span[contains(text(),'PRICE')]")
  WebElement sortPrice;

  @FindBy(xpath = "//div[@class='clr']//span[starts-with(@class,'alignItemsCenter')]/span")
  List<WebElement> priceList;

  private WaitUtil waitUtil;
  private int explicitTimeOut;
  private HelperUtil helperUtil;

  public SearchResultsPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    waitUtil = new WaitUtil(driver);
    explicitTimeOut = waitUtil.getExplicitTimeout();
    helperUtil = new HelperUtil(driver);
  }

  public void waitForFareTrendsDialogToBeVisible() {
    waitUtil.isElementVisible(fareTrends, explicitTimeOut);
  }

  public void verifyPriceFilterExists() {
    waitUtil.isElementVisible(sortPrice, explicitTimeOut);
  }

  public void clickOnPriceFilter() {
    sortPrice.click();
  }

  public void getCostOfAllFlightsSortedFromHighToLow() {
    List<Integer> priceListInDescendingOrder =
        priceList
            .stream()
            .map(
                costOfEachFlight ->
                    Integer.parseInt(costOfEachFlight.getText().replaceAll(",", "")))
            .collect(Collectors.toCollection(LinkedList::new));
    helperUtil.verifyPriceSortedFromHighToLow(priceListInDescendingOrder);
  }
}
