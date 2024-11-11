@F-002
Feature: F-002 : Get Judicial Bookings

  Background:
    Given an appropriate test context as detailed in the test data source

  @S-011
  Scenario: must successfully receive single booking
    Given a user with [an active IDAM profile with full permissions],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    When a request is prepared with appropriate values,
    And the request [contains an authenticated user id having only single booking],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookings].

  @S-012
  Scenario: must successfully receive multiple bookings
    Given a user with [an active IDAM profile with full permissions],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    And a successful call [to create a booking] as in [CreationBaseDataForBookingBeftaUser1],
    When a request is prepared with appropriate values,
    And the request [contains an authenticated user id having multiple bookings],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingsBeftaUser1].

  @S-013
  Scenario: must successfully receive bookings without X-Correlation-ID Header
    Given a user with [an active IDAM profile with full permissions],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    When a request is prepared with appropriate values,
    And the request [does not have X-Correlation-ID header],
    And the request [contains an user id having booking],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookings].

  @S-014
  Scenario: must receive an error response for an invalid user id
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains an invalid user id],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a negative response is received,
    And the response has all other details as expected.

  @S-016
  Scenario: must successfully receive bookings for multiple users
    Given a user [BeftaUser1 - with an active IDAM profile],
    And a user [BeftaUser2 - with an active IDAM profile],
    And a successful call [to create a booking] as in [CreationBaseDataForBookingBeftaUser1],
    And a successful call [to create a booking] as in [CreationBaseDataForBookingBeftaUser2],
    When a request is prepared with appropriate values,
    And the request [contains multiple users],
    And the request [comes from an approved S2S serivce that is also permitted to bypass the UserId validation],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingsBeftaUser1],
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingsBeftaUser2].

  @S-017
  Scenario: must receive an error response when querying a different users bookings
    Given a user [BeftaUser1 - with an active IDAM profile],
    And a user [BeftaUser2 - with an active IDAM profile],
    And a successful call [to create a booking] as in [CreationBaseDataForBookingBeftaUser2],
    When a request is prepared with appropriate values,
    And the request [contains a call for a different users bookings],
    And the request [comes from an approved S2S serivce that is not permitted to bypass the UserId validation],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Judicial Booking Service],
    Then a negative response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingsBeftaUser2].
