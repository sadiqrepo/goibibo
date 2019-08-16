package model;

/** Created by sadiq on 29/07/19. */
public class FlightSearchModel {

  protected String yourLocation;
  private String destination;
  private String departureDate;

  public String getYourLocation() {
    return yourLocation;
  }

  public void setYourLocation(String yourLocation) {
    this.yourLocation = yourLocation;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }
}
