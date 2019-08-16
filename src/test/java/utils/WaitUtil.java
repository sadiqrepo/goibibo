package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by sadiq on 30/07/19.
 */
public class WaitUtil {

    private WebDriver driver;

    public WaitUtil(WebDriver driver)
    {
        this.driver = driver;
    }

    public int getExplicitTimeout()
    {
        //Gets the predefined explicit wait from the config.reader & then returns the same in integer format
        return Integer.parseInt(ConfigReader.get("explicit.wait"));

    }


    public Boolean isElementVisible(WebElement element, final long timeout) {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        }
        catch (ElementNotVisibleException e)
        {
            LogUtil.error("\n"+element+ "not found on the page. Here are the exception details: ");
            LogUtil.error(String.valueOf(e));
        }
        return false;
    }

}
