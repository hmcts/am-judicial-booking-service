package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WireMockStubs.OBJECT_MAPPER;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WireMockStubs.SERVICE_NAME_EXUI;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WireMockStubs.SERVICE_NAME_ORM;

public class QueryBookingIntegrationTest extends BaseAuthorisedTestIntegration {

    @Test
    public void rejectRequestWithoutBody() throws Exception {
        getRequestSpecification(SERVICE_NAME_EXUI)
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Required request body is missing"));
    }

    @Test
    public void rejectRequestWithoutUsers() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(UserRequest.builder().build());

        getRequestSpecification()
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Provided list of userIds is empty"));
    }

    @Test
    public void rejectRequestWithInvalidUserFormat() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(UserRequest.builder().userIds(List.of("abc-12")).build());

        getRequestSpecification()
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The input parameter: \\\"abc-12\\\", "
                        + "does not comply with the required pattern"));
    }

    @Test
    public void retrieveEmptyJudicialBookings_nonExistingUser() throws Exception {
        String randomUserId = UUID.randomUUID().toString();

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(randomUserId)).build());

        getRequestSpecification(SERVICE_NAME_ORM, randomUserId)
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void retrieveJudicialBookings_queryDifferentUser_shouldFail() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID2)).build());

        // NB: EXUI is NOT on the bypass validation list: i.e. user can only query their own bookings
        getRequestSpecification(SERVICE_NAME_EXUI, ACTOR_ID1) // i.e. different user
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void retrieveJudicialBookings_queryDifferentUser_shouldPassIfS2SAllowsValidationBypass() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID2)).build());

        // NB: ORM is on the bypass validation list: i.e. service can query any bookings
        getRequestSpecification(SERVICE_NAME_ORM, ACTOR_ID1) // i.e. different user
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_validSingleBooking() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID2)).build());

        String response = getRequestSpecification(SERVICE_NAME_EXUI, ACTOR_ID2)
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
        BookingQueryResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingQueryResponse.class
        );
        assertNotNull(bookingResponse);
        List<BookingEntity> actualBookings = bookingResponse.getBookingEntities();
        assertNotNull(actualBookings);
        actualBookings.forEach(actual -> Assertions.assertAll(
                () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
                () -> assertEquals(ACTOR_ID2, actual.getUserId())
        ));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_querySingleUser_multipleBookings() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID1)).build());

        String response = getRequestSpecification(SERVICE_NAME_EXUI, ACTOR_ID1)
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
        BookingQueryResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingQueryResponse.class
        );
        assertNotNull(bookingResponse);
        List<BookingEntity> actualBookings = bookingResponse.getBookingEntities();
        assertNotNull(actualBookings);
        actualBookings.forEach(actual -> Assertions.assertAll(
                () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
                () -> assertEquals(ACTOR_ID1, actual.getUserId())
        ));
    }

    @Test
    public void retrieveJudicialBooking_queryMultipleUsers_shouldFail() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID1, ACTOR_ID2)).build());

        // NB: EXUI is NOT on the bypass validation list: i.e. user can only query their own bookings
        getRequestSpecification(SERVICE_NAME_EXUI, ACTOR_ID1)
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_queryMultipleUsers_shouldPassIfS2SAllowsValidationBypass() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID1, ACTOR_ID2)).build());

        // NB: ORM is on the bypass validation list: i.e. service can query any bookings, including multiple users
        String response = getRequestSpecification(SERVICE_NAME_ORM, ACTOR_ID1)
                .body(OBJECT_MAPPER.writeValueAsString(request))
                .when().post(QUERY_URL)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().body().asString();
        BookingQueryResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingQueryResponse.class
        );
        assertNotNull(bookingResponse);
        List<BookingEntity> actualBookings = bookingResponse.getBookingEntities();
        assertNotNull(actualBookings);
        actualBookings.forEach(actual -> Assertions.assertAll(
                () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
                () -> assertThat(actual.getUserId(), anyOf(is(ACTOR_ID1), is(ACTOR_ID2)))
        ));
    }

}
