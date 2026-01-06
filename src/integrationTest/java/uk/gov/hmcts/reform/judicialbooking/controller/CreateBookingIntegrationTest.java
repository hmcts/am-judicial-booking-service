package uk.gov.hmcts.reform.judicialbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.parsing.Parser;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.judicialbooking.client.IdamOpenId;
import uk.gov.hmcts.reform.judicialbooking.client.S2sClient;
import uk.gov.hmcts.reform.judicialbooking.config.TestConfigProperties;
import uk.gov.hmcts.reform.judicialbooking.controller.utils.MockUtils;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequestWrapper;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;

@ActiveProfiles("itest")
@ExtendWith({SerenityJUnit5Extension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource({"classpath:application-itest.yaml"})
public class CreateBookingIntegrationTest {
    private static final String URL = "/am/bookings";
    private static final String SERVICE_HEADER = "ServiceAuthorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACTOR_ID1 = "631d322c-eea7-4d53-bd92-e6ec51bcb390";

    private static final String REGION = "region";
    private static final String LOCATION = "location";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static String s2sToken;
    private static IdamOpenId idamOpenIdClient;
    private String baseUrl = "http://localhost";

    @Value("${idam.s2s-auth.totp_secret}")
    protected String s2sSecret;

    @Value("${idam.s2s-auth.url}")
    protected String s2sUrl;

    @Value("${idam.s2s-auth.microservice}")
    protected String s2sName;

    @LocalServerPort
    private int port;

    @Autowired
    private TestConfigProperties configProperties;

    @BeforeEach
    public void init() {
        baseUrl = baseUrl.concat(":").concat(port + "");
        SerenityRest.useRelaxedHTTPSValidation();
        SerenityRest.setDefaultParser(Parser.JSON);

        if (null == s2sToken) {
            s2sToken = new S2sClient(s2sUrl, s2sName, s2sSecret).signIntoS2S();
        }

        if (null == idamOpenIdClient) {
            idamOpenIdClient = new IdamOpenId(configProperties);
        }
    }

    @Test
    public void rejectRequestWithoutBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SERVICE_HEADER, "Bearer " + s2sToken);
        headers.add(AUTHORIZATION_HEADER, "Bearer " + idamOpenIdClient.getcwdAdminOpenIdToken( "caseworker-civil-admin"));
        headers.setContentType(MediaType.APPLICATION_JSON);

        SerenityRest.with()
                .relaxedHTTPSValidation()
                .baseUri(baseUrl)
                .headers(headers)
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
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
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

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .headers(MockUtils.getHttpHeaders(MockUtils.S2S_JBS))
                .body(mapper.writeValueAsBytes(request))
                .when().post(URL)
                .then().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
