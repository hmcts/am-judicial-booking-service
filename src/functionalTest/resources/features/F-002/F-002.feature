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
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookings].

  @S-012
  Scenario: must successfully receive multiple bookings
    Given a user with [an active IDAM profile with full permissions],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    When a request is prepared with appropriate values,
    And the request [contains an authenticated user id having multiple bookings],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookings].

#  @S-013
#  Scenario: must receive an error response for a non-authenticated user id
#    Given a user with [an active IDAM profile with full permissions],
#    When a request is prepared with appropriate values,
#    And the request [contains a non-authenticated user id],
#    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Booking Service],
#    Then a negative response is received,
#    And the response has all other details as expected.

  @S-014
  Scenario: must successfully receive bookings without X-Correlation-ID Header
    Given a user with [an active IDAM profile with full permissions],
    And a successful call [to create a booking] as in [CreationBaseDataForBooking],
    When a request is prepared with appropriate values,
    And the request [does not have X-Correlation-ID header],
    And the request [contains an user id having booking],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookings].

  @S-015
  Scenario: must receive an error response for an invalid user id
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains an invalid user id],
    And it is submitted to call the [Get Bookings by Authenticated User Id] operation of [Booking Service],
    Then a negative response is received,
    And the response has all other details as expected.
