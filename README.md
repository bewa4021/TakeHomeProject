# Take Home Challenge
- By Ben Wasko
- For Campspot

## Running Instructions:
#### 1) In your terminal of choice, navigate to your '/TakeHomeChallenge' Directory (where this file lives)
- Run the command:
    - (Mac) `./gradlew build run --args="JSON_Input/test-case.json"`
    - (Windows) `gradlew build run --args="JSON_Input\\test-case.json"`
- Expected output:
    - Comfy Cabin
    - Rickety Cabin
    - Cabin in the woods
#### 2) Alternatively, run via Intellij
- Open '/TakeHomeChallenge' project as a gradle project in Intellij
- Run the Main.java file within the `TakeHomeChallenge` Package
- Configure the Program Arguments to be `"JSON_Input/test-case.json"`

Note: I ran this project on two different machines, but that does not guarantee it will work for everyone. Please reach out with questions at `Benjamin.Wasko@colorado.edu`.

## High Level Description:

This project is broken into three Packages:
1) The DataModel
- This is the data structure for the objects in the JSON file, including:
    - `ReservationSearch` (This is the "search" object in the JSON)
    - `Campsite` (This is the "campsite" object in the JSON)
    - `Reservation` (This is the "reservation" object in the JSON)
2) The Parser
- This section reads the JSON file and parses each object and field into the corresponding Java equivalents defined in the DataModel. The parser throws exceptions for incorrect JSONs. For example, it ensures that dates are correctly formatted in "yyyy-MM-dd" and that an object's start date does not occur not after its end date.
- The parser creates the correct Java objects from the JSON input and passes that as a Map of Strings to Objects, back to the main method.
3) The Reservation Finder
- `ReservationFinder` has the main search function `findCampsitesForReservationWindow` and a handful of helper functions. This main search function determines if a campsite can be reserved in the desired window by checking if the `ReservationSearch` window conflicts with any existing reservations. Conflicting reservations include overlapping dates or creating a gap in reservations which violates the gap rule.
- If an existing reservation conflicts with the search window of the new reservation, that campsite is removed from the list of valid campsites.
- `findCampsitesForReservtionWindow` ultimately returns a list of valid campsites printed by the main function.

## Assumptions:
1) I assumed that a search for a reservation will be one or more days.
2) I assumed that the existing reservations were valid (do not violate the gap rule or overlap with other reservations for the same campsite).
