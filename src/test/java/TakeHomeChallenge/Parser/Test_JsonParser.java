package TakeHomeChallenge.Parser;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Test_JsonParser {

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

  @Test
  public void parse() throws ParseException, IOException {
    String filePath = "JSON_Input/parser-test.json";

    Map<String, Object> parsedObjects = JsonParser.parse(filePath);

    ReservationSearch reservationSearch =
        (ReservationSearch) parsedObjects.get(JsonConstants.SEARCH_FIELD);
    Date searchStartDate = dateFormat.parse("2018-06-03");
    Date searchEndDate = dateFormat.parse("2018-06-03");

    assertEquals(reservationSearch.getStartDate(), searchStartDate);
    assertEquals(reservationSearch.getEndDate(), searchEndDate);

    List<Campsite> campsites = (List<Campsite>) parsedObjects.get(JsonConstants.CAMPSITES_FIELD);
    int campsiteId = 1;
    String campsiteName = "Cozy Cabin";

    assertEquals(1, campsites.size());
    assertEquals(campsiteId, campsites.get(0).getId());
    assertEquals(campsiteName, campsites.get(0).getName());

    List<Reservation> reservations =
        (List<Reservation>) parsedObjects.get(JsonConstants.RESERVATIONS_FIELD);
    int res1CampId = 1;
    Date res1StartDate = dateFormat.parse("2018-06-01");
    Date res1EndDate = dateFormat.parse("2018-06-01");
    int res2CampId = 1;
    Date res2StartDate = dateFormat.parse("2018-06-02");
    Date res2EndDate = dateFormat.parse("2018-06-02");

    assertEquals(2, reservations.size());
    assertEquals(res1CampId, reservations.get(0).getCampsiteId());
    assertEquals(res1StartDate, reservations.get(0).getStartDate());
    assertEquals(res1EndDate, reservations.get(0).getEndDate());
    assertEquals(res2CampId, reservations.get(1).getCampsiteId());
    assertEquals(res2StartDate, reservations.get(1).getStartDate());
    assertEquals(res2EndDate, reservations.get(1).getEndDate());
  }
}
