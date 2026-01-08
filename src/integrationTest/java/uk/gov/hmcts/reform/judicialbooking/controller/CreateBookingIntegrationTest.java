package uk.gov.hmcts.reform.judicialbooking.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.MockUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.WiremockFixtures;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequestWrapper;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;

@ExtendWith({SerenityJUnit5Extension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateBookingIntegrationTest extends BaseTestIntegration {
    private static final String URL = "/am/bookings";
    private static final String ACTOR_ID1 = "631d322c-eea7-4d53-bd92-e6ec51bcb390";

    private static final String REGION = "region";
    private static final String LOCATION = "location";

    private String baseUrl = "http://localhost";

    @Value("${server.port}")
    private int serverPort;

    @BeforeEach
    public void init() {
        SerenityRest.useRelaxedHTTPSValidation();
        SerenityRest.setDefaultParser(Parser.JSON);
    }

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

    //    @Test
    //    public void createJudicialBookingsMandatoryValues() throws Exception {
    //        var request = new BookingRequest(null, null, null, LocalDate.now(),
    //                LocalDate.now());
    //
    //        final MvcResult result = mockMvc.perform(post(URL)
    //                        .contentType(JSON_CONTENT_TYPE)
    //                        .headers(getHttpHeaders())
    //                        .content(mapper.writeValueAsBytes(new BookingRequestWrapper(request))))
    //                .andExpect(status().isCreated())
    //                .andReturn();
    //        BookingResponse response = mapper.readValue(
    //                result.getResponse().getContentAsString(),
    //                BookingResponse.class
    //        );
    //        assertNotNull(response);
    //        BookingEntity actualBooking = response.getBookingResponseEntity();
    //        assertNotNull(actualBooking);
    //        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
    //        assertEquals(actualBooking.getUserId(), ACTOR_ID1);
    //    }

    //    @Test
    //    public void createJudicialBookingFullValues() throws Exception {
    //
    //        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
    //                LocalDate.now());
    //
    //        final MvcResult result = mockMvc.perform(post(URL)
    //                        .contentType(JSON_CONTENT_TYPE)
    //                        .headers(getHttpHeaders())
    //                        .content(mapper.writeValueAsBytes(new BookingRequestWrapper(request))))
    //                .andExpect(status().isCreated())
    //                .andReturn();
    //        BookingResponse response = mapper.readValue(
    //                result.getResponse().getContentAsString(),
    //                BookingResponse.class
    //        );
    //        assertNotNull(response);
    //        BookingEntity actualBooking = response.getBookingResponseEntity();
    //        assertNotNull(actualBooking);
    //        assertEquals(request.getLocationId(), actualBooking.getLocationId());
    //        assertEquals(request.getRegionId(), actualBooking.getRegionId());
    //        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
    //        assertEquals(actualBooking.getUserId(), ACTOR_ID1);
    //    }

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

    //    @Test
    //    public void createBookingWithInputUserId() throws Exception {
    //
    //        var request = new BookingRequest(ACTOR_ID1, REGION, LOCATION,
    //                LocalDate.now(), LocalDate.now().plusDays(1));
    //
    //        final MvcResult result = mockMvc.perform(post(URL)
    //                        .contentType(JSON_CONTENT_TYPE)
    //                        .headers(getHttpHeaders())
    //                        .content(mapper.writeValueAsBytes(new BookingRequestWrapper(request))))
    //                .andExpect(status().isCreated())
    //                .andReturn();
    //        BookingResponse response = mapper.readValue(
    //                result.getResponse().getContentAsString(),
    //                BookingResponse.class
    //        );
    //        assertNotNull(response);
    //        BookingEntity actualBooking = response.getBookingResponseEntity();
    //        assertNotNull(actualBooking);
    //        assertEquals(request.getUserId(), actualBooking.getUserId());
    //        assertEquals(request.getLocationId(), actualBooking.getLocationId());
    //        assertEquals(request.getRegionId(), actualBooking.getRegionId());
    //        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
    //    }

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

    private RequestSpecification getRequestSpecification() {
        return SerenityRest.given()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl)
                .port(serverPort)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS));
    }
}
