package TakeHomeChallenge;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;
import TakeHomeChallenge.Parser.JsonConstants;
import TakeHomeChallenge.Parser.JsonParser;
import TakeHomeChallenge.ReservationService.ReservationFinder;

import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    try {
      Map<String, Object> parsedObjects = JsonParser.parse(args[0]);
      ReservationSearch reservationSearch =
          (ReservationSearch) parsedObjects.get(JsonConstants.SEARCH_FIELD);
      List<Campsite> campsites = (List<Campsite>) parsedObjects.get(JsonConstants.CAMPSITES_FIELD);
      List<Reservation> reservations =
          (List<Reservation>) parsedObjects.get(JsonConstants.RESERVATIONS_FIELD);
      Map<Integer, Campsite> validCampsites =
          ReservationFinder.findCampsitesForReservationWindow(
              reservationSearch, reservations, campsites);
      validCampsites.values().stream().forEach(campsite -> System.out.println(campsite.getName()));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
