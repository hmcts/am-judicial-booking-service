package uk.gov.hmcts.reform.judicialbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequestWrapper;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WireMockStubs.OBJECT_MAPPER;

class CreateBookingIntegrationTest extends BaseAuthorisedTestIntegration {

    @Test
    void rejectRequestWithoutBody() throws Exception {
        getRequestSpecification()
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Required request body is missing"));
    }

    @Test
    void rejectRequestWithoutRegion() throws Exception {
        var request = new BookingRequest(null, null, LOCATION, LocalDate.now(),
                LocalDate.now());
        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("RegionId cannot be Null or Empty, if LocationId is available"));
    }

    @Test
    void rejectRequestWithoutStartDate() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, null,
                LocalDate.now());
        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Begin date cannot be Null or Empty"));
    }

    @Test
    void rejectRequestWithoutEndDate() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                null);
        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("End date cannot be Null or Empty"));
    }

    @Test
    void createJudicialBookingsMandatoryValues() throws Exception {
        var request = new BookingRequest(null, null, null, LocalDate.now(),
                LocalDate.now());

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().asString();
        BookingResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingResponse.class
        );
        assertNotNull(bookingResponse);
        BookingEntity actualBooking = bookingResponse.getBookingResponseEntity();
        assertNotNull(actualBooking);
        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
        assertEquals(ACTOR_ID1, actualBooking.getUserId());
    }

    @Test
    void createJudicialBookingFullValues() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now());

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().asString();
        BookingResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingResponse.class
        );
        assertNotNull(bookingResponse);
        BookingEntity actualBooking = bookingResponse.getBookingResponseEntity();
        assertNotNull(actualBooking);
        assertEquals(request.getLocationId(), actualBooking.getLocationId());
        assertEquals(request.getRegionId(), actualBooking.getRegionId());
        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
        assertEquals(ACTOR_ID1, actualBooking.getUserId());
    }

    @Test
    void rejectBookingRequestExpiredEndDate() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now().minusDays(1));

        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The end time: " + LocalDate.now().minusDays(1)
                        + " takes place before the current time: " + LocalDate.now()));
    }

    @Test
    void rejectBookingRequestGreaterStartDate() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().plusDays(5), LocalDate.now());

        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The end time: " + LocalDate.now()
                        + " takes place before the begin time: " + LocalDate.now().plusDays(5)));
    }

    @Test
    void rejectBookingRequestExpiredDates() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));

        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The begin time: " + LocalDate.now().minusDays(5)
                        + " takes place before the current time: " + LocalDate.now()));
    }

    @Test
    void createBookingWithInputUserId() throws Exception {

        var request = new BookingRequest(ACTOR_ID1, REGION, LOCATION,
                LocalDate.now(), LocalDate.now().plusDays(1));

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().asString();
        BookingResponse bookingResponse = OBJECT_MAPPER.readValue(
                response,
                BookingResponse.class
        );
        assertNotNull(bookingResponse);
        BookingEntity actualBooking = bookingResponse.getBookingResponseEntity();
        assertNotNull(actualBooking);
        assertEquals(request.getUserId(), actualBooking.getUserId());
        assertEquals(request.getLocationId(), actualBooking.getLocationId());
        assertEquals(request.getRegionId(), actualBooking.getRegionId());
        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
    }

    @Test
    void createBookingWithInvalidInputUserId() throws Exception {
        var request = new BookingRequest(UUID.randomUUID().toString(), REGION, LOCATION,
                LocalDate.now(), LocalDate.now().plusDays(1));

        getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(CREATE_BOOKING_URL)
                .then().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

}
