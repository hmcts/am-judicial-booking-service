@F-001
Feature: F-001 : Create Judicial Bookings

  Background:
    Given an appropriate test context as detailed in the test data source

  @S-001
  Scenario: must successfully create single booking for fee pay judge with only mandatory fields
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains a single booking with only mandatory fields],
    And it is submitted to call the [Create Booking] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingResponse].

  @S-002
  Scenario: must successfully create single booking for fee pay judge with all fields
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains a single booking with all fields],
    And it is submitted to call the [Create Booking] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingResponse].

  @S-003
  Scenario: must receive a Reject response in logs when creation of booking is not successful
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains booking request has invalid data],
    And it is submitted to call the [Create Booking] operation of [Judicial Booking Service],
    Then a negative response is received,
    And the response has all other details as expected.

  @S-004
  Scenario: must successfully create single booking for fee pay judge with userId param
    Given a user with [an active IDAM profile with full permissions],
    When a request is prepared with appropriate values,
    And the request [contains a single booking with userId param],
    And it is submitted to call the [Create Booking] operation of [Judicial Booking Service],
    Then a positive response is received,
    And the response has all other details as expected,
    And a successful call [to delete bookings just created above] as in [DeleteDataForBookingResponse].
