package TakeHomeChallenge.Parser;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;
import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonParser {

  private static final String SEARCH_FIELD = JsonConstants.SEARCH_FIELD;
  private static final String CAMPSITES_FIELD = JsonConstants.CAMPSITES_FIELD;
  private static final String RESERVATIONS_FIELD = JsonConstants.RESERVATIONS_FIELD;

  private static final String CAMPSITE_ID = JsonConstants.CAMPSITE_ID;
  private static final String ID = JsonConstants.ID;
  private static final String END_DATE = JsonConstants.END_DATE;
  private static final String NAME = JsonConstants.NAME;
  private static final String START_DATE = JsonConstants.START_DATE;

  private static final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public static Map<String, Object> parse(String jsonPath) throws IOException, ParseException {
    Map<String, Object> inputData = new HashMap<>();

    String json = getJSON(jsonPath);
    JsonElement jsonTree = parser.parse(json);
    if (jsonTree.isJsonObject()) {
      JsonObject jsonObject = jsonTree.getAsJsonObject();

      ReservationSearch reservationSearch = getSearchObjectFromJson(jsonObject);
      inputData.put(SEARCH_FIELD, reservationSearch);

      List<Campsite> campsites = getCampsitesFromJson(jsonObject);
      inputData.put(CAMPSITES_FIELD, campsites);

      List<Reservation> reservations = getReservationsFromJson(jsonObject);
      inputData.put(RESERVATIONS_FIELD, reservations);
    }
    return inputData;
  }

  private static String getJSON(String filePath) throws IOException {
    try {
      String content =
          new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.ISO_8859_1);
      return content;
    } catch (IOException e) {
      throw e;
    }
  }

  private static ReservationSearch getSearchObjectFromJson(JsonObject jsonObject)
      throws IOException, ParseException {
    ReservationSearch reservationSearch = null;

    if (jsonObject.get(SEARCH_FIELD) != null) {
      JsonObject searchObject = jsonObject.get(SEARCH_FIELD).getAsJsonObject();

      if (searchObject.get(END_DATE) == null || searchObject.get(START_DATE) == null) {
        throw new IOException("The Search object must contain a start date and an end date");
      } else {
        Date endDate = dateFormat.parse(searchObject.get(END_DATE).getAsString());
        Date startDate = dateFormat.parse(searchObject.get(START_DATE).getAsString());
        if (startDate.after(endDate)) {
          throw new IOException("Search start date must be before or equal to end date");
        }
        reservationSearch = new ReservationSearch(startDate, endDate);
      }
    }
    if (reservationSearch == null) {
      throw new IOException("Json must include a search object");
    } else {
      return reservationSearch;
    }
  }

  private static List<Campsite> getCampsitesFromJson(JsonObject jsonObject) throws IOException {
    List<Campsite> campsites = new ArrayList<>();

    if (jsonObject.get(CAMPSITES_FIELD) != null) {
      JsonElement campsitesJson = jsonObject.get(CAMPSITES_FIELD);
      if (campsitesJson.isJsonArray()) {
        JsonArray campsiteArray = campsitesJson.getAsJsonArray();

        for (JsonElement campsiteElement : campsiteArray) {

          JsonObject campsiteJson = campsiteElement.getAsJsonObject();
          if (campsiteJson.get(ID) == null || campsiteJson.get(NAME) == null) {
            throw new IOException("The Campsite Object must contain an id and a name");
          } else {
            int campsiteId = campsiteJson.get(ID).getAsInt();
            String campsiteName = campsiteJson.get(NAME).getAsString();
            Campsite campsite = new Campsite(campsiteId, campsiteName);
            campsites.add(campsite);
          }
        }
      }
    }

    return campsites;
  }

  private static List<Reservation> getReservationsFromJson(JsonObject jsonObject)
      throws IOException, ParseException {
    List<Reservation> reservations = new ArrayList<>();

    if (jsonObject.get(RESERVATIONS_FIELD) != null) {
      JsonElement reservationsJson = jsonObject.get(RESERVATIONS_FIELD);
      if (reservationsJson.isJsonArray()) {
        JsonArray reservationArray = reservationsJson.getAsJsonArray();

        for (JsonElement reservationElement : reservationArray) {
          JsonObject reservationJson = reservationElement.getAsJsonObject();
          if (reservationJson.get(CAMPSITE_ID) == null
              || reservationJson.get(END_DATE) == null
              || reservationJson.get(END_DATE) == null) {
            throw new IOException(
                "The Reservation Object must contain a campsiteId, a start date, and an end date");
          } else {
            Date endDate = dateFormat.parse(reservationJson.get(END_DATE).getAsString());
            Date startDate = dateFormat.parse(reservationJson.get(START_DATE).getAsString());
            if (startDate.after(endDate)) {
              throw new IOException("Reservation start date must be before or equal to end date");
            }
            int campsiteId = reservationJson.get(CAMPSITE_ID).getAsInt();
            Reservation reservation = new Reservation(campsiteId, startDate, endDate);
            reservations.add(reservation);
          }
        }
      }
    }

    return reservations;
  }
}
