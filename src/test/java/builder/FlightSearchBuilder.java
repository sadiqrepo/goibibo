package builder;

import model.FlightSearchModel;

/** Created by sadiq on 29/07/19. */
public class FlightSearchBuilder extends FlightSearchModel {

  FlightSearchModel flightSearchModel = new FlightSearchModel();

  public FlightSearchBuilder(String[] flightSearchDetails) {

    flightSearchModel.setYourLocation(flightSearchDetails[0]);
    flightSearchModel.setDestination(flightSearchDetails[1]);
  }

  public FlightSearchModel getFlightDetails() {
    return flightSearchModel;
  }
}
