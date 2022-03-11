package uk.gov.hmcts.reform.judicialbooking.controller;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateBookingIntegrationTest extends BaseTest {
    private static final String URL = "/am/bookings";

    private static final String ACTOR_ID1 = "631d322c-eea7-4d53-bd92-e6ec51bcb390";

    private MockMvc mockMvc;

    private static final String REGION = "region";
    private static final String LOCATION = "location";

    private static final String EXPRESSION = "$.errorDescription";

    @Inject
    private WebApplicationContext wac;

    @MockBean
    SecurityUtils securityUtilsMock;

    @MockBean
    private LDClientInterface ldClient;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MockitoAnnotations.openMocks(this);
        doReturn(true).when(ldClient).isFlagKnown(anyString());
        doReturn(true).when(ldClient).boolVariation(anyString(), any(LDUser.class), eq(false));
        doReturn(ACTOR_ID1).when(securityUtilsMock).getUserId();
    }

    @Test
    public void shouldRejectRequestWithLDFlagDisable() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now());
        doReturn(false).when(ldClient).boolVariation(anyString(), any(LDUser.class), eq(false));

        mockMvc.perform(post(URL).contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("Forbidden: Insufficient permissions: "
                                + "Launch Darkly flag is not enabled for the endpoint")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutBody() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE).headers(getHttpHeaders()))
                .andExpect(status().is(400))
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("Required request body is missing")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutRegion() throws Exception {
        var request = new BookingRequest(null, null, LOCATION, LocalDate.now(),
                LocalDate.now());
        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("RegionId cannot be Null or Empty, if LocationId is available")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutStartDate() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, null,
                LocalDate.now());
        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("Begin date cannot be Null or Empty")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutEndDate() throws Exception {
        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                null);
        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("End date cannot be Null or Empty")))
                .andReturn();
    }

    @Test
    public void createJudicialBookingsMandatoryValues() throws Exception {
        var request = new BookingRequest(null, null, null, LocalDate.now(),
                LocalDate.now());

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();
        BookingResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingResponse.class
        );
        assertNotNull(response);
        BookingEntity actualBooking = response.getBookingResponseEntity();
        assertNotNull(actualBooking);
        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
        assertEquals(actualBooking.getUserId(), ACTOR_ID1);
    }

    @Test
    public void createJudicialBookingFullValues() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION, LocalDate.now(),
                LocalDate.now());

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();
        BookingResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingResponse.class
        );
        assertNotNull(response);
        BookingEntity actualBooking = response.getBookingResponseEntity();
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

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("The end time: " + LocalDate.now().minusDays(1)
                                + " takes place before the current time: " + LocalDate.now())))
                .andReturn();
    }

    @Test
    public void rejectBookingRequestGreaterStartDate() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().plusDays(5), LocalDate.now());

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("The end time: " + LocalDate.now()
                                + " takes place before the begin time: " + LocalDate.now().plusDays(5))))
                .andReturn();
    }

    @Test
    public void rejectBookingRequestExpiredDates() throws Exception {

        var request = new BookingRequest(null, REGION, LOCATION,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(EXPRESSION)
                        .value(containsString("The begin time: " + LocalDate.now().minusDays(5)
                                + " takes place before the current time: " + LocalDate.now())))
                .andReturn();
    }

    @Test
    public void createBookingWithInputUserId() throws Exception {

        var request = new BookingRequest("1234-abcd", REGION, LOCATION,
                LocalDate.now(), LocalDate.now().plusDays(1));

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andReturn();
        BookingResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingResponse.class
        );
        assertNotNull(response);
        BookingEntity actualBooking = response.getBookingResponseEntity();
        assertNotNull(actualBooking);
        assertEquals(request.getUserId(), actualBooking.getUserId());
        assertEquals(request.getLocationId(), actualBooking.getLocationId());
        assertEquals(request.getRegionId(), actualBooking.getRegionId());
        assertEquals(request.getEndDate().plusDays(1), actualBooking.getEndTime().toLocalDate());
    }

}
