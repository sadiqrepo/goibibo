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

/**
 * Created by sadiq on 23/07/19.
 */
public class BaseTest {

    public static WebDriver driver;
    public String browserSelected;


    private static Logger _log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    void Config()
    {
        ConfigWriter.setPropertyValue("logger.file", "webLogger", ConfigReader.get("config.path"));
    }


    /**
     * Methods to find OS type and Browser time to launch the webdriver
     * @param browserName
     */

    @Parameters({"browser"})
    @BeforeClass
    public void LaunchBrowser(String browserName){
        try
        {
            if (browserName.equalsIgnoreCase("Chrome"))
            {
                LogUtil.info("Selected browser is: "+browserName);
                String chromePath = ConfigReader.get("chromedriver.path");
                System.setProperty("webdriver.chrome.driver", chromePath);

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments("disable-infobars");

                driver = new ChromeDriver(options);
                browserSelected = "chrome";
            }
            else if (browserName.equalsIgnoreCase("ie"))
            {
                LogUtil.info("Selected browser is: "+browserName);
                driver = new InternetExplorerDriver();
                browserSelected = "ie";
            }

            else if (browserName.equalsIgnoreCase("firefox"))
            {
                LogUtil.info("Selected browser is: "+browserName);

                String fireFoxPath = getFireFoxPath();
                System.setProperty("webdriver.gecko.driver", fireFoxPath);

                driver = fireFoxWebDriver();

                FirefoxProfile ffp = new FirefoxProfile();
                ffp.setPreference("accessibility.blockautorefresh", true);
                browserSelected = "firefox";
            }
            else if (browserName.equalsIgnoreCase("chromeheadless")) {
                LogUtil.info("Selected browser is: " + browserName);
                String chromePath = ConfigReader.get("chromedriver.path");
                System.setProperty("webdriver.chrome.driver", chromePath);

                ChromeOptions options = new ChromeOptions();
                options.addArguments("headless");
                options.addArguments("window-size=1200x600"); //Never remove this line for chrome headless. Otherwise CI will fail
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
    public void tearDownBrowser()
    {
        driver.quit();
    }

    private boolean getFirefoxHeadlessStatus(String currPlatform)
    {
        if(currPlatform.contains("mac"))
        {
            return Boolean.parseBoolean(ConfigReader.get("set.firefox.headless.on.mac"));
        }
        else if(currPlatform.contains("linux"))
        {
            return Boolean.parseBoolean(ConfigReader.get("set.firefox.headless.on.linux"));
        }
        else
        {
            return true;
        }
    }

    private void setHeadlessStatus(String currentOS, FirefoxOptions FF_Options)
    {
        if(getFirefoxHeadlessStatus(currentOS))
        {
            FF_Options.addArguments("--headless");
        }
    }

    private DesiredCapabilities setFirefoxOptions()
    {
        FirefoxOptions options = new FirefoxOptions();
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("moz:firefoxOptions", options);
        capabilities.setCapability("accessibility.blockautorefresh", true);
        setHeadlessStatus("mac", options);   //Enable this to make the firefox headless

        return capabilities;
    }

    private String getFireFoxPath()
    {
        String firePath = null;
        if(SystemUtils.IS_OS_LINUX)
        {
            LogUtil.debug("\n[DEBUG] Detected os is LINUX.\n Geckodriver for Linux 64bit will be used");
            firePath = ConfigReader.get("geckodriver.path.linux");
        }
        else if (SystemUtils.IS_OS_MAC)
        {
            LogUtil.debug("\n[DEBUG] Detected os is Mac. \n Geckodriver for Mac OS will be used");
            firePath = ConfigReader.get("geckodriver.path.mac");
        }
        else
        {
            LogUtil.debug("\n\n[DEBUG] Unable to detect the current OS....!!!\nGeckodriver cannot be used\n\n");
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






/*


    public static void main(String[] args) {

       System.setProperty("webdriver.chrome.driver", "./src/test/resources/driver/chromedriver");
        WebDriver driver = new ChromeDriver();


        String baseUrl = "https://www.goibibo.com";

        // Open the goibibo page
        driver.get(baseUrl);

        try {
            driver.findElement(By.xpath("//input[@placeholder='Departure']")).click();
        }catch (InvalidSelectorException ex){
            ex.getMessage();
        }

        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);


       // WebElement dateWidget = driver.findElement(By.xpath("//div[@class='DayPicker DayPicker--en']"));
        List<WebElement> columns=driver.findElements(By.xpath("//div[@class='DayPicker DayPicker--en']//div[@class='calDate']"));

        //comparing the text of cell with today's date and clicking it.
        for (WebElement cell : columns)
        {
            if (cell.getText().equals(date))
            {
                cell.click();
                break;
            }


        }


        driver.findElement(By.id("gosuggest_inputSrc")).sendKeys("Bangalore");

        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ul[@role='listbox']//li")));

        List<WebElement> listItems = driver.findElements(By.xpath("//ul[@role='listbox']//li"));

        listItems.get(0).click();


        driver.findElement(By.id("gosuggest_inputDest")).sendKeys("Mumbai");

        WebDriverWait wait1 = new WebDriverWait(driver,30);
        wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//ul[@role='listbox']//li")));

        List<WebElement> listItems1 = driver.findElements(By.xpath("//ul[@role='listbox']//li"));

        listItems1.get(0).click();


        driver.findElement(By.id("gi_search_btn")).click();


        WebDriverWait wait2 = new WebDriverWait(driver,30);
        wait2.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("fareTrendsDiv")));


        scrollToBottomOfFlightSearchPage(driver);


        */
/*WebElement element = driver.findElement(By.id("showAllFlights"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

        WebElement element = driver.findElement(By.id("showAllFlights"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
        element.click();
        WebDriverWait wait3 = new WebDriverWait(driver,30);
        wait3.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("showAllFlights")));
        element.click();*//*



        scrollToTopOfFlightSearchPage(driver);

        WebDriverWait wait4 = new WebDriverWait(driver,30);
        wait4.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("sortByPriceOnw")));


        driver.findElement(By.id("sortByPriceOnw")).click();

        List<WebElement> priceHighToLow  = driver.findElements(By.xpath("//div[@class='cardContainer']//span[starts-with(@class,'fr')]//span[starts-with(@id,'o')]"));

        List<Integer> priceList  = new LinkedList<>();



        for(WebElement price : priceHighToLow){
            priceList.add(Integer.parseInt(price.getText().replaceAll(",","")));

        }

       for(int price : priceList ){
            System.out.println(price);
        }

        verifyPriceSortedInDecreasingOrderOfCost(priceList);


    }



    public static void scrollToBottomOfFlightSearchPage(WebDriver driver){

        WebElement lastElement =
                driver.findElement(By.id("footer"));
        int y = lastElement.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,"+y+")");

    }

    public static void scrollToTopOfFlightSearchPage(WebDriver driver){

        WebElement lastElement =
                driver.findElement(By.id("searchWrapOuterBox"));
        int y = lastElement.getLocation().getY();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,"+y+")");

    }

    public static Boolean verifyPriceSortedInDecreasingOrderOfCost(List<Integer> comparePriceList){

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



    public static WebElement isElementVisible(WebDriver driver, By locator, final long timeout){
        WebElement element = null;

        try{
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        }catch(ElementNotVisibleException e){
            e.printStackTrace();
        }

        return element;
    }


*/


}
