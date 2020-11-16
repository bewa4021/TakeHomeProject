package TakeHomeChallenge.ReservationService;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test_ReservationFinder {

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

  @Test
  public void addDaysToDateProperlyAddsOneDay() throws ParseException {
    Date inputDate = dateFormat.parse("2020-01-01");
    Date expectedOutput = dateFormat.parse("2020-01-02");
    int numberOfDaysToAdd = 1;

    Date actualOutput = ReservationFinder.addDaysToDate(inputDate, numberOfDaysToAdd);

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void addDaysToDateWillProperlyChangeMonths() throws ParseException {
    Date inputDate = dateFormat.parse("2020-01-30");
    Date expectedOutput = dateFormat.parse("2020-02-03");
    int numberOfDaysToAdd = 4;

    Date actualOutput = ReservationFinder.addDaysToDate(inputDate, numberOfDaysToAdd);

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void addDaysToDateWillProperlyChangeYears() throws ParseException {
    Date inputDate = dateFormat.parse("2020-12-15");
    Date expectedOutput = dateFormat.parse("2021-01-04");
    int numberOfDaysToAdd = 20;

    Date actualOutput = ReservationFinder.addDaysToDate(inputDate, numberOfDaysToAdd);

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void addDaysToDateWillProperlyHandleLeapYears() throws ParseException {
    Date inputDate = dateFormat.parse("2016-02-20");
    Date expectedOutput = dateFormat.parse("2016-03-01");
    int numberOfDaysToAdd = 10;

    Date actualOutput = ReservationFinder.addDaysToDate(inputDate, numberOfDaysToAdd);

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void reservationBlocksConflictWhenEndDateIsAfterStartDate() throws ParseException {
    Date endOfFirstBlock = dateFormat.parse("2020-11-17");
    Date startOfSecondBlock = dateFormat.parse("2020-11-15");
    int gap = 1;

    assertTrue(
        ReservationFinder.reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap));
  }

  @Test
  public void reservationBlocksConflictWhenEndDateIsAfterStartDateRegardlessOfGap()
      throws ParseException {
    Date endOfFirstBlock = dateFormat.parse("2020-11-17");
    Date startOfSecondBlock = dateFormat.parse("2020-11-15");
    int gap = 0;

    assertTrue(
        ReservationFinder.reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap));
  }

  @Test
  public void reservationBlocksDoNotConflictWhenThereIsNoGapBetween() throws ParseException {
    Date endOfFirstBlock = dateFormat.parse("2020-11-17");
    Date startOfSecondBlock = dateFormat.parse("2020-11-18");
    int gap = 1;

    assertFalse(
        ReservationFinder.reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap));
  }

  @Test
  public void reservationBlocksDoNotConflictWhenThereIsNoGapBetweenRegardlessOfGapRule()
      throws ParseException {
    Date endOfFirstBlock = dateFormat.parse("2020-11-17");
    Date startOfSecondBlock = dateFormat.parse("2020-11-18");
    int gap = 5;

    assertFalse(
        ReservationFinder.reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap));
  }

  @Test
  public void reservationBlocksDoNotConflictWhenGapIsLargerThanGapRule() throws ParseException {
    Date endOfFirstBlock = dateFormat.parse("2020-11-17");
    Date startOfSecondBlock = dateFormat.parse("2020-11-20");
    int gap = 1;

    assertFalse(
        ReservationFinder.reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap));
  }

  @Test
  public void getReservationsByCampsiteIdProperlyDividesReservations() {
    Date randomDate = new Date();
    Reservation res1Camp1 = new Reservation(1, randomDate, randomDate);
    Reservation res2Camp1 = new Reservation(1, randomDate, randomDate);
    Reservation res1Camp2 = new Reservation(2, randomDate, randomDate);
    List<Reservation> reservations = Arrays.asList(res1Camp1, res2Camp1, res1Camp2);

    Map<Integer, List<Reservation>> reservationsByCampsiteId =
        ReservationFinder.getReservationsByCampsiteId(reservations);
    reservationsByCampsiteId.forEach(
        (Integer key, List<Reservation> reservationList) -> {
          reservationList
              .stream()
              .forEach(reservation -> assertEquals(key, (Integer) reservation.getCampsiteId()));
        });
  }

  @Test
  public void otherReservationExistsInGapChecksForOtherReservationsInGapRuleGap()
      throws ParseException {
    Date startBlock1 = dateFormat.parse("2020-11-15");
    Date endBlock1 = dateFormat.parse("2020-11-15");
    Date startBlock2 = dateFormat.parse("2020-11-16");
    Date endBlock2 = dateFormat.parse("2020-11-16");
    Date startSearchDate = dateFormat.parse("2020-11-17");
    Date endSearchDate = dateFormat.parse("2020-11-18");

    Reservation res1 = new Reservation(1, startBlock1, endBlock1);
    Reservation res2 = new Reservation(1, startBlock2, endBlock2);
    ReservationSearch searchWindow = new ReservationSearch(startSearchDate, endSearchDate);

    List<Reservation> reservations = Arrays.asList(res1, res2);

    // This should be a valid configuration of reservations, but a one day gap exists
    // between res1 and the search window. which makes the reservations conflict
    assertTrue(
        ReservationFinder.reservationBlocksConflict(
            res1.getEndDate(), searchWindow.getStartDate(), 1));

    // A Check for other reservations existing in that gap are needed to solve this case
    assertTrue(
        ReservationFinder.otherReservationExistsInGap(reservations, 1, endBlock1, startSearchDate));
  }

  @Test
  public void findCampsitesForReservationWindow() throws ParseException {
    Date startBlock1 = dateFormat.parse("2020-11-15");
    Date endBlock1 = dateFormat.parse("2020-11-15");
    Date startBlock2 = dateFormat.parse("2020-11-16");
    Date endBlock2 = dateFormat.parse("2020-11-16");
    Date startSearchDate = dateFormat.parse("2020-11-16");
    Date endSearchDate = dateFormat.parse("2020-11-18");

    Reservation res1 = new Reservation(1, startBlock1, endBlock1);
    Reservation res2 = new Reservation(1, startBlock2, endBlock2);
    List<Reservation> reservations = Arrays.asList(res1, res2);
    ReservationSearch searchWindow = new ReservationSearch(startSearchDate, endSearchDate);

    Campsite campsite1 = new Campsite(1, "campsite1");
    Campsite campsite2 = new Campsite(2, "campsite2");
    List<Campsite> campsites = Arrays.asList(campsite1, campsite2);

    Map<Integer, Campsite> validCampsites =
        ReservationFinder.findCampsitesForReservationWindow(searchWindow, reservations, campsites);

    assertEquals(validCampsites.keySet().size(), 1);
    assertEquals(validCampsites.values().toArray()[0], campsite2);
  }
}
