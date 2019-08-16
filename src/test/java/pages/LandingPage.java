package pages;

import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** Created by sadiq on 14/08/19. */
public class LandingPage extends BasePage {

  private WaitUtil waitUtil;
  private int explicitTimeOut;

  @FindBy(xpath = "//input[@placeholder='Departure']")
  WebElement departure;

  @FindBy(xpath = "//div[@class='DayPicker DayPicker--en']//div[@class='calDate']")
  List<WebElement> calendarDateColumns;

  @FindBy(id = "gosuggest_inputSrc")
  WebElement source;

  @FindBy(xpath = "//ul[@role='listbox']//li")
  List<WebElement> sourceSuggestions;

  @FindBy(id = "gosuggest_inputDest")
  WebElement destination;

  @FindBy(xpath = "//ul[@role='listbox']//li")
  List<WebElement> destinationSuggestions;

  @FindBy(id = "gi_search_btn")
  WebElement searchButton;

  public LandingPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
    waitUtil = new WaitUtil(driver);
    explicitTimeOut = waitUtil.getExplicitTimeout();
  }

  public void enterDepartureDate() {
    try {
      departure.click();
    } catch (InvalidSelectorException ex) {
      ex.getMessage();
    }

    DateFormat dateFormat = new SimpleDateFormat("dd");
    Date today = Calendar.getInstance().getTime();
    String date = dateFormat.format(today);

    //comparing the text of cell with today's date and clicking it.
    for (WebElement cell : calendarDateColumns) {
      if (cell.getText().equals(date)) {
        cell.click();
        break;
      }
    }
  }

  public void enterSourceLocation(String value) {
    source.sendKeys(value);
    waitUtil.isElementsListVisible(sourceSuggestions, explicitTimeOut);
    sourceSuggestions.get(0).click();
  }

  public void enterDestinationLocation(String value) {
    destination.sendKeys(value);
    waitUtil.isElementsListVisible(destinationSuggestions, explicitTimeOut);
    destinationSuggestions.get(0).click();
  }

  public void clickSearchButton() {
    searchButton.click();
  }
}
