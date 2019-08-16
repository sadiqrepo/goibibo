package scenario;

import builder.FlightSearchBuilder;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LandingPage;
import pages.SearchResultsPage;
import utils.ConfigReader;
import utils.HelperUtil;
import utils.LogUtil;
import utils.CsvReader;

import java.io.File;
import java.util.List;

/** Created by sadiq on 07/08/19. */
public class SearchFlightsTest extends BaseTest {

  private static Logger _log = LoggerFactory.getLogger(SearchFlightsTest.class);

  String sTestCaseName = this.getClass().getSimpleName().trim();

  private CsvReader readCsv = new CsvReader();
  String baseUrl = ConfigReader.get("baseURL");

  public LandingPage landingPage;
  public SearchResultsPage searchResultsPage;
  public HelperUtil helperUtil;

  @BeforeClass
  public void setUp() {

    LogUtil.info("Before Login");
    LogUtil.startTestCase(sTestCaseName);

    landingPage = PageFactory.initElements(driver, LandingPage.class);
    searchResultsPage = PageFactory.initElements(driver, SearchResultsPage.class);
    helperUtil = new HelperUtil(driver);
  }

  @DataProvider(name = "searchFlights")
  public Object[][] searchFlights() {
    Object[][] rawData;
    List<String[]> searchFlights =
        readCsv.parseFile(
            new File(ConfigReader.get("FlightSearchDetailsCSVPATH")).getAbsolutePath());
    rawData = new Object[searchFlights.size()][1];
    for (int index = 0; index < searchFlights.size(); index++) {
      for (String[] searchFlight : searchFlights) {
        rawData[index++][0] = new FlightSearchBuilder(searchFlight);
      }
    }
    return rawData;
  }

  @Test(description = "SearchFlights", dataProvider = "searchFlights")
  public void searchFlight(FlightSearchBuilder flightSearchBuilder) {
    helperUtil.navigatePage(baseUrl);
    landingPage.validateLandingPageisDisplayed();
    landingPage.enterDepartureDate();
    landingPage.enterSourceLocation(flightSearchBuilder.getFlightDetails().getYourLocation());
    landingPage.enterDestinationLocation(flightSearchBuilder.getFlightDetails().getDestination());
    landingPage.clickSearchButton();
    searchResultsPage.waitForFareTrendsDialogToBeVisible();
    searchResultsPage.verifyPriceFilterExists();
    searchResultsPage.clickOnPriceFilter();
    searchResultsPage.getCostOfAllFlightsSortedFromHighToLow();
  }

  @AfterTest(description = "After test info")
  public void afterTest() {
    LogUtil.endTestCase(sTestCaseName);
    driver.quit();
  }
}
