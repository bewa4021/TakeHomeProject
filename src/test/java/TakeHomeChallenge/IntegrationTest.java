package TakeHomeChallenge;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;
import TakeHomeChallenge.Parser.JsonConstants;
import TakeHomeChallenge.Parser.JsonParser;
import TakeHomeChallenge.ReservationService.ReservationFinder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IntegrationTest {

  @Test
  public void GivenTestCase() throws IOException, ParseException {
    String testFilePath = "JSON_Input/test-case.json";

    Map<String, Object> parsedObjects = JsonParser.parse(testFilePath);
    ReservationSearch reservationSearch =
        (ReservationSearch) parsedObjects.get(JsonConstants.SEARCH_FIELD);
    List<Campsite> campsites = (List<Campsite>) parsedObjects.get(JsonConstants.CAMPSITES_FIELD);
    List<Reservation> reservations =
        (List<Reservation>) parsedObjects.get(JsonConstants.RESERVATIONS_FIELD);
    Map<Integer, Campsite> validCampsitesById =
        ReservationFinder.findCampsitesForReservationWindow(
            reservationSearch, reservations, campsites);

    List<Campsite> validCampsites = new ArrayList<>();
    validCampsites.addAll(validCampsitesById.values());
    List<String> actualCampsiteNames =
        Arrays.asList("Comfy Cabin", "Rickety Cabin", "Cabin in the Woods");

    assertEquals(3, validCampsites.size());

    int iterator = 0;
    for (Campsite validCampsite : validCampsites) {
      assertEquals(validCampsite.getName(), actualCampsiteNames.get(iterator));
      iterator++;
    }
  }

  @Test
  public void testCase2() throws IOException, ParseException {
    String testFilePath = "JSON_Input/test-case2.json";

    Map<String, Object> parsedObjects = JsonParser.parse(testFilePath);
    ReservationSearch reservationSearch =
        (ReservationSearch) parsedObjects.get(JsonConstants.SEARCH_FIELD);
    List<Campsite> campsites = (List<Campsite>) parsedObjects.get(JsonConstants.CAMPSITES_FIELD);
    List<Reservation> reservations =
        (List<Reservation>) parsedObjects.get(JsonConstants.RESERVATIONS_FIELD);
    Map<Integer, Campsite> validCampsitesById =
        ReservationFinder.findCampsitesForReservationWindow(
            reservationSearch, reservations, campsites);

    List<Campsite> validCampsites = new ArrayList<>();
    validCampsites.addAll(validCampsitesById.values());
    List<String> actualCampsiteNames = Arrays.asList("Campsite2", "Campsite3");

    assertEquals(2, validCampsites.size());

    int iterator = 0;
    for (Campsite validCampsite : validCampsites) {
      assertEquals(validCampsite.getName(), actualCampsiteNames.get(iterator));
      iterator++;
    }
  }
}
