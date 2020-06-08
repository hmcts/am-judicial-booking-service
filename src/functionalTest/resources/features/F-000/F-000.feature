@F-000
Feature: Access Judicial Booking API

  Background:
    Given an appropriate test context as detailed in the test data source

  @S-000
  Scenario: must access Judicial Booking API
    Given a user with [an active caseworker profile],
    When a request is prepared with appropriate values,
    And the request [is to be made on behalf of Judicial Booking API],
    And it is submitted to call the [Access Judicial Booking API] operation of [Judicial Booking API],
    Then a positive response is received,
    And the response has all other details as expected.
