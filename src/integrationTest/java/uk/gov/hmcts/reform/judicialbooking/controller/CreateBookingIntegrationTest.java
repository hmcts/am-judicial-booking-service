package uk.gov.hmcts.reform.judicialbooking.controller;

import com.nimbusds.jose.JOSEException;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.suite.api.ConfigurationParameter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.MockUtils;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequestWrapper;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.ACTOR_ID1;
import static uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures.OBJECT_MAPPER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateBookingIntegrationTest extends BaseTestIntegration {
    private static final String URL = "/am/bookings";

    private static final String REGION = "region";
    private static final String LOCATION = "location";
    private static final String BASEURL = "http://localhost";

    @LocalServerPort
    private int serverPort;

    @Test
    public void rejectRequestWithoutBody() throws Exception {
        getRequestSpecification()
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Required request body is missing"));
    }

    @Test
    public void rejectRequestWithoutRegion() throws Exception {
        var request = new BookingRequestWrapper(new BookingRequest(null, null, LOCATION, LocalDate.now(),
                LocalDate.now()));
        getRequestSpecification()
                .body(mapper.writeValueAsBytes(request))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("RegionId cannot be Null or Empty, if LocationId is available"));
    }

    @Test
    public void rejectRequestWithoutStartDate() throws Exception {
        var request = new BookingRequestWrapper(new BookingRequest(null, REGION, LOCATION, null,
                LocalDate.now()));
        getRequestSpecification()
                .body(mapper.writeValueAsBytes(request))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("Begin date cannot be Null or Empty"));
    }

    @Test
    public void rejectRequestWithoutEndDate() throws Exception {
        var request = new BookingRequestWrapper(new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                null));
        getRequestSpecification()
                .body(mapper.writeValueAsBytes(request))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("End date cannot be Null or Empty"));
    }

    @Test
    public void createJudicialBookingsMandatoryValues() throws Exception {
        var request = new BookingRequest(null, null, null, LocalDate.now(),
                LocalDate.now());

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(URL)
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
        assertEquals(actualBooking.getUserId(), ACTOR_ID1);
    }

    @Test
    public void createJudicialBookingFullValues() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now());

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(URL)
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
        assertEquals(actualBooking.getUserId(), ACTOR_ID1);
    }

    @Test
    public void rejectBookingRequestExpiredEndDate() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now().minusDays(1));

        getRequestSpecification()
                .body(mapper.writeValueAsBytes(new BookingRequestWrapper(request)))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The end time: " + LocalDate.now().minusDays(1)
                        + " takes place before the current time: " + LocalDate.now()));
    }

    @Test
    public void rejectBookingRequestGreaterStartDate() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().plusDays(5), LocalDate.now());

        getRequestSpecification()
                .body(mapper.writeValueAsBytes(new BookingRequestWrapper(request)))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The end time: " + LocalDate.now()
                        + " takes place before the begin time: " + LocalDate.now().plusDays(5)));
    }

    @Test
    public void rejectBookingRequestExpiredDates() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));

        getRequestSpecification()
                .body(mapper.writeValueAsBytes(new BookingRequestWrapper(request)))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .and()
                .body(containsString("The begin time: " + LocalDate.now().minusDays(5)
                                + " takes place before the current time: " + LocalDate.now()));
    }

    @Test
    public void createBookingWithInputUserId() throws Exception {

        var request = new BookingRequest(ACTOR_ID1, REGION, LOCATION,
                LocalDate.now(), LocalDate.now().plusDays(1));

        String response = getRequestSpecification()
                .body(OBJECT_MAPPER
                        .writeValueAsString(new BookingRequestWrapper(request)))
                .when().post(URL)
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
    public void createBookingWithInvalidInputUserId() throws Exception {
        var request = new BookingRequestWrapper(new BookingRequest(UUID.randomUUID().toString(), REGION, LOCATION,
                LocalDate.now(), LocalDate.now().plusDays(1)));

        getRequestSpecification()
                .body(mapper.writeValueAsBytes(request))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    private RequestSpecification getRequestSpecification() throws JOSEException {
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .baseUri(BASEURL)
                .port(serverPort)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS));
    }
}
