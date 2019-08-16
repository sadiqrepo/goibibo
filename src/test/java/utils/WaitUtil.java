package utils;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

/** Created by sadiq on 30/07/19. */
public class WaitUtil {

  private WebDriver driver;

  public WaitUtil(WebDriver driver) {
    this.driver = driver;
  }

  public int getExplicitTimeout() {
    //Gets the predefined explicit wait from the config.reader & then returns the same in integer format
    return Integer.parseInt(ConfigReader.get("explicit.wait"));
  }

  public Boolean isElementVisible(WebElement element, final long timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.visibilityOf(element));
      return true;
    } catch (ElementNotVisibleException e) {
      LogUtil.error("\n" + element + "not found on the page. Here are the exception details: ");
      LogUtil.error(String.valueOf(e));
    }
    return false;
  }

  public Boolean isElementsListVisible(List<WebElement> elements, final long timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.visibilityOfAllElements(elements));
      return true;
    } catch (ElementNotVisibleException e) {
      LogUtil.error("\n" + elements + "not found on the page. Here are the exception details: ");
      LogUtil.error(String.valueOf(e));
    }
    return false;
  }

  public WebElement fluentWait(final By locator, final long timeout, final long polltime) {
    Wait<WebDriver> wait =
        new FluentWait<WebDriver>(driver)
            .withTimeout(timeout, TimeUnit.SECONDS)
            .pollingEvery(polltime, TimeUnit.MILLISECONDS)
            .ignoring(NoSuchElementException.class);

    WebElement element =
        wait.until(
            new Function<WebDriver, WebElement>() {
              public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
              }
            });

    return element;
  }

  public static void sleep() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {
      LogUtil.info("[INFO] Exception seen while using Thread.sleep()");
      LogUtil.info(String.valueOf(ignored));
    }
  }

  //Is alert present
  public Alert isAlertPresent(final long timeout) {
    Alert element = null;

    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      element = wait.until(ExpectedConditions.alertIsPresent());
    } catch (ElementNotVisibleException e) {
      LogUtil.error("\n[Error] Alert is not present. Here's the exception's stack trace");
      LogUtil.error(String.valueOf(e));
    }
    return element;
  }

  public static void setSleepTimeOut(final long sleepWait) {
    try {
      Thread.sleep(sleepWait);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void sleep(Integer timeInMillis) {
    try {
      Thread.sleep(timeInMillis);
    } catch (InterruptedException ignored) {
    }
  }

  public void setImplicitTimeOut(final long implicitWait) {
    try {
      driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }
}
