package TakeHomeChallenge.ReservationService;

import TakeHomeChallenge.DataModel.Campsite;
import TakeHomeChallenge.DataModel.Reservation;
import TakeHomeChallenge.DataModel.ReservationSearch;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationFinder {

  public static Map<Integer, Campsite> findCampsitesForReservationWindow(
      ReservationSearch searchWindow,
      List<Reservation> existingReservations,
      List<Campsite> possibleCampsites) {
    return findCampsitesForReservationWindow(
        searchWindow, existingReservations, possibleCampsites, 1);
  }

  public static Map<Integer, Campsite> findCampsitesForReservationWindow(
      ReservationSearch searchWindow,
      List<Reservation> existingReservations,
      List<Campsite> possibleCampsites,
      int gap) {

    Map<Integer, Campsite> campsiteByCampsiteId =
        possibleCampsites
            .stream()
            .collect(Collectors.toMap(campsite -> campsite.getId(), campsite -> campsite));

    existingReservations
        .stream()
        .forEach(
            existingReservation -> {
              Date endOfFirstBlock;
              Date startOfSecondBlock;

              if (existingReservation.getStartDate().before(searchWindow.getStartDate())) {
                endOfFirstBlock = existingReservation.getEndDate();
                startOfSecondBlock = searchWindow.getStartDate();
              } else {
                endOfFirstBlock = searchWindow.getEndDate();
                startOfSecondBlock = existingReservation.getStartDate();
              }

              if (reservationBlocksConflict(endOfFirstBlock, startOfSecondBlock, gap)
                  && !otherReservationExistsInGap(
                      existingReservations,
                      existingReservation.getCampsiteId(),
                      endOfFirstBlock,
                      startOfSecondBlock)) {
                campsiteByCampsiteId.remove(existingReservation.getCampsiteId());
              }
            });
    return campsiteByCampsiteId;
  }

  public static boolean reservationBlocksConflict(
      Date endOfFirstBlock, Date startOfSecondBlock, int gap) {
    Date modifiedExistingDate = addDaysToDate(endOfFirstBlock, 1);
    Date modifiedExistingDatePlusGap = addDaysToDate(modifiedExistingDate, gap);
    return !modifiedExistingDate.equals(startOfSecondBlock)
        && !modifiedExistingDatePlusGap.before(startOfSecondBlock);
  }

  public static Date addDaysToDate(Date date, int numberOfDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
    Date newDate = calendar.getTime();
    return newDate;
  }

  public static boolean otherReservationExistsInGap(
      List<Reservation> reservations,
      int campsiteId,
      Date endOfFirstBlock,
      Date startOfSecondBlock) {
    Map<Integer, List<Reservation>> reservationsByCampsiteId =
        getReservationsByCampsiteId(reservations);
    List<Reservation> allReservationsForCampsite = reservationsByCampsiteId.get(campsiteId);
    for (Reservation reservation : allReservationsForCampsite) {
      if (reservation.getStartDate().after(endOfFirstBlock)
          && reservation.getStartDate().before(startOfSecondBlock)) {
        return true;
      }
    }
    return false;
  }

  public static Map<Integer, List<Reservation>> getReservationsByCampsiteId(
      List<Reservation> allReservations) {
    Map<Integer, List<Reservation>> reservationsByCampsiteId = new HashMap<>();
    allReservations
        .stream()
        .forEach(
            reservation -> {
              if (reservationsByCampsiteId.containsKey(reservation.getCampsiteId())) {
                reservationsByCampsiteId.get(reservation.getCampsiteId()).add(reservation);
              } else {
                List<Reservation> reservations = new ArrayList<>();
                reservations.add(reservation);
                reservationsByCampsiteId.put(reservation.getCampsiteId(), reservations);
              }
            });
    return reservationsByCampsiteId;
  }
}
