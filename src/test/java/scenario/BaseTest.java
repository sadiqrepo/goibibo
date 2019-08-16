package scenario;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import utils.ConfigReader;
import utils.ConfigWriter;
import utils.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;

import static java.util.concurrent.TimeUnit.SECONDS;

/** Created by sadiq on 23/07/19. */
public class BaseTest {

  public static WebDriver driver;
  public String browserSelected;

  private static Logger _log = LoggerFactory.getLogger(BaseTest.class);

  @BeforeSuite(alwaysRun = true)
  void Config() {
    ConfigWriter.setPropertyValue("logger.file", "webLogger", ConfigReader.get("config.path"));
  }

  /**
   * Methods to find OS type and Browser time to launch the webdriver
   *
   * @param browserName
   */
  @Parameters({"browser"})
  @BeforeClass
  public void LaunchBrowser(String browserName) {
    try {
      if (browserName.equalsIgnoreCase("Chrome")) {
        LogUtil.info("Selected browser is: " + browserName);
        String chromePath = ConfigReader.get("chromedriver.path");
        System.setProperty("webdriver.chrome.driver", chromePath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("disable-infobars");

        driver = new ChromeDriver(options);
        browserSelected = "chrome";
      } else if (browserName.equalsIgnoreCase("ie")) {
        LogUtil.info("Selected browser is: " + browserName);
        driver = new InternetExplorerDriver();
        browserSelected = "ie";
      } else if (browserName.equalsIgnoreCase("firefox")) {
        LogUtil.info("Selected browser is: " + browserName);

        String fireFoxPath = getFireFoxPath();
        System.setProperty("webdriver.gecko.driver", fireFoxPath);

        driver = fireFoxWebDriver();

        FirefoxProfile ffp = new FirefoxProfile();
        ffp.setPreference("accessibility.blockautorefresh", true);
        browserSelected = "firefox";
      } else if (browserName.equalsIgnoreCase("chromeheadless")) {
        LogUtil.info("Selected browser is: " + browserName);
        String chromePath = ConfigReader.get("chromedriver.path");
        System.setProperty("webdriver.chrome.driver", chromePath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments(
            "window-size=1200x600"); //Never remove this line for chrome headless. Otherwise CI will fail
        options.addArguments("disable-notifications");
        options.addArguments("disable-gpu");

        driver = new ChromeDriver(options);
        browserSelected = "chromeHeadlessBrowser";
      }

      driver.manage().window().setPosition(new Point(0, 0));
      Dimension d = new Dimension(1300, 900);
      driver.manage().window().setSize(d);

    } catch (WebDriverException e) {
      System.out.println(e.getMessage());
    }
  }

  @AfterClass
  public void tearDownBrowser() {
    driver.quit();
  }

  private boolean getFirefoxHeadlessStatus(String currPlatform) {
    if (currPlatform.contains("mac")) {
      return Boolean.parseBoolean(ConfigReader.get("set.firefox.headless.on.mac"));
    } else if (currPlatform.contains("linux")) {
      return Boolean.parseBoolean(ConfigReader.get("set.firefox.headless.on.linux"));
    } else {
      return true;
    }
  }

  private void setHeadlessStatus(String currentOS, FirefoxOptions FF_Options) {
    if (getFirefoxHeadlessStatus(currentOS)) {
      FF_Options.addArguments("--headless");
    }
  }

  private DesiredCapabilities setFirefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    capabilities.setCapability("moz:firefoxOptions", options);
    capabilities.setCapability("accessibility.blockautorefresh", true);
    setHeadlessStatus("mac", options); //Enable this to make the firefox headless

    return capabilities;
  }

  private String getFireFoxPath() {
    String firePath = null;
    if (SystemUtils.IS_OS_LINUX) {
      LogUtil.debug("\n[DEBUG] Detected os is LINUX.\n Geckodriver for Linux 64bit will be used");
      firePath = ConfigReader.get("geckodriver.path.linux");
    } else if (SystemUtils.IS_OS_MAC) {
      LogUtil.debug("\n[DEBUG] Detected os is Mac. \n Geckodriver for Mac OS will be used");
      firePath = ConfigReader.get("geckodriver.path.mac");
    } else {
      LogUtil.debug(
          "\n\n[DEBUG] Unable to detect the current OS....!!!\nGeckodriver cannot be used\n\n");
    }

    return firePath;
  }

  private WebDriver fireFoxWebDriver() {
    System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
    if (SystemUtils.IS_OS_LINUX) {
      FirefoxOptions options = new FirefoxOptions();
      setHeadlessStatus("linux", options);

      return new FirefoxDriver(options);
    } else {
      FirefoxOptions opts = new FirefoxOptions(setFirefoxOptions());
      return new FirefoxDriver(opts);
    }
  }
}
