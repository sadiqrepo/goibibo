package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Created by sadiq on 09/08/19. */
public class HelperUtil {

  private WebDriver driver;

  public HelperUtil(WebDriver driver) {
    this.driver = driver;
  }

  public void navigatePage(String url) {
    driver.get(url);
  }

  public Boolean verifyPriceSortedFromHighToLow(List<Integer> comparePriceList) {

    int currentMaxValue = comparePriceList.get(0);

    for (int i = 1; i < comparePriceList.size(); i++) {

      if (currentMaxValue < comparePriceList.get(i)) {
        LogUtil.info("The list is not sorted in High to low");
        return false;
      }
      currentMaxValue = comparePriceList.get(i);
    }
    LogUtil.info("The list is Sorted in High to low");
    return true;
  }

  public void scrollToSpecificLocationInPage(WebDriver driver, WebElement element) {
    int y = element.getLocation().getY();
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollTo(0," + y + ")");
  }
}
