package uk.gov.hmcts.reform.judicialbooking.controller;


import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QueryBookingIntegrationTest extends BaseTest {

    private static final String URL = "/am/bookings/query";

    private static final String ACTOR_ID1 = "631d322c-eea7-4d53-bd92-e6ec51bcb390";
    private static final String ACTOR_ID2 = "123e4567-e89b-42d3-a456-556642445678";
    private MockMvc mockMvc;

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
    }

    @Test
    public void shouldRejectRequestWithLDFlagDisable() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(UserRequest.builder().build());
        doReturn(false).when(ldClient).isFlagKnown(anyString());

        mockMvc.perform(post(URL).contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorDescription")
                        .value(containsString("Resource not found: "
                                + "The flag jbs-query-bookings-api-flag is not configured in Launch Darkly")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutBody() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE).headers(getHttpHeaders()))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorDescription")
                        .value(containsString("Required request body is missing")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithoutUsers() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(UserRequest.builder().build());

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescription")
                        .value(containsString("Provided list of userIds is empty")))
                .andReturn();
    }

    @Test
    public void rejectRequestWithInvalidUserFormat() throws Exception {
        BookingQueryRequest request = new BookingQueryRequest(UserRequest.builder().userIds(List.of("abc-12")).build());

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescription")
                        .value(containsString("The input parameter: \"abc-12\", "
                                + "does not comply with the required pattern")))
                .andReturn();
    }

    @Test
    public void retrieveEmptyJudicialBookings_nonExistingUser() throws Exception {
        String randomUserId = UUID.randomUUID().toString();

        doReturn(randomUserId).when(securityUtilsMock).getUserId();

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(randomUserId)).build());

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void retrieveJudicialBookingsInvalidUser() throws Exception {

        doReturn(ACTOR_ID1).when(securityUtilsMock).getUserId();

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(UUID.randomUUID().toString())).build());

        mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_validSingleBooking() throws Exception {

        doReturn(ACTOR_ID2).when(securityUtilsMock).getUserId();

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID2)).build());

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        BookingQueryResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingQueryResponse.class
        );
        assertNotNull(response);
        List<BookingEntity> actualBookings = response.getBookingEntities();
        assertNotNull(actualBookings);
        actualBookings.forEach(actual -> Assertions.assertAll(
            () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
            () -> assertEquals(actual.getUserId(), ACTOR_ID2)
        ));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_validMultipleBooking() throws Exception {

        doReturn(ACTOR_ID1).when(securityUtilsMock).getUserId();

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID1)).build());

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        BookingQueryResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingQueryResponse.class
        );
        assertNotNull(response);
        List<BookingEntity> actualBookings = response.getBookingEntities();
        assertNotNull(actualBookings);
        actualBookings.forEach(actual -> Assertions.assertAll(
            () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
            () -> assertEquals(actual.getUserId(), ACTOR_ID1)
        ));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"classpath:sql/insert_judicial_bookings.sql"})
    public void retrieveJudicialBooking_validMultipleUsers() throws Exception {

        BookingQueryRequest request = new BookingQueryRequest(
                UserRequest.builder().userIds(List.of(ACTOR_ID1, ACTOR_ID2)).build());

        final MvcResult result = mockMvc.perform(post(URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .headers(getHttpHeaders())
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        BookingQueryResponse response = mapper.readValue(
                result.getResponse().getContentAsString(),
                BookingQueryResponse.class
        );
        assertNotNull(response);
        List<BookingEntity> actualBookings = response.getBookingEntities();
        assertNotNull(actualBookings);

        actualBookings.forEach(actual -> Assertions.assertAll(
            () -> assertTrue(actual.getEndTime().isAfter(ZonedDateTime.now())),
            () -> assertThat(actual.getUserId(), anyOf(is(ACTOR_ID1), is(ACTOR_ID2)))
        ));
    }

}
