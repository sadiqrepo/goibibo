package pages;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import utils.*;

/** Created by sadiq on 30/07/19. */
public class BasePage {

  public WebDriver driver;
  public WaitUtil waitUtil;

  private static Logger _log = LoggerFactory.getLogger(BasePage.class);

  public BasePage(WebDriver driver) {
    this.driver = driver;
    waitUtil = new WaitUtil(driver);
  }

  @BeforeSuite(alwaysRun = true)
  void Config() {
    ConfigWriter.setPropertyValue("logger.file", "webLogger", ConfigReader.get("config.path"));
  }
}
