package uk.gov.hmcts.reform.judicialbooking.launchdarkly;

import com.launchdarkly.sdk.server.LDClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ForbiddenException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class FeatureConditionEvaluationTest {

    private final SecurityUtils securityUtils = mock(SecurityUtils.class);

    private final LDClient ldClient = mock(LDClient.class);

    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @InjectMocks
    private final FeatureConditionEvaluation sut = new FeatureConditionEvaluation();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
            "/am/bookings,POST,jbs-create-bookings-api-flag",
            "/am/bookings/query,POST,jbs-query-bookings-api-flag"
    })
    void getLdFlagWithValidFlagMapEntries(String url, String method, String flag) {
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        String flagName = sut.getLaunchDarklyFlag(request);
        Assertions.assertEquals(flag, flagName);
    }

    @ParameterizedTest
    @CsvSource({
            "GET",
            "DELETE",
            "POST",
            "POST",
            "INVALID",
    })
    void getLdFlagWithNonExistingUriPath(String method) {
        when(request.getRequestURI()).thenReturn("/am/dummy");
        when(request.getMethod()).thenReturn(method);
        String flagName = sut.getLaunchDarklyFlag(request);
        Assertions.assertNull(flagName);
    }

    @Test
    void evaluateLdFlag() {
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(true);
        Assertions.assertTrue(sut.isFlagEnabled("serviceName", "userName"));
    }

    @Test
    void evaluateLdFlagFalse() {
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(false);
        Assertions.assertFalse(sut.isFlagEnabled("falseServiceName", "falseUserName"));
    }

    @Test
    void isValidFlag() {
        when(ldClient.isFlagKnown("serviceName")).thenReturn(true);
        Assertions.assertTrue(sut.isValidFlag("serviceName"));
    }

    @Test
    void isValidFlagReturnsFalse() {
        when(ldClient.isFlagKnown("falseServiceName")).thenReturn(false);
        Assertions.assertFalse(sut.isValidFlag("falseServiceName"));
    }

    //@ParameterizedTest
    @CsvSource({
            "/am/bookings,POST,jbs-create-bookings-api-flag",
            "/am/bookings/query,POST,jbs-query-bookings-api-flag"
    })
    void preHandle_happy(String url, String method, String flag) {
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(true);
        when(ldClient.isFlagKnown(flag)).thenReturn(true);
        Assertions.assertTrue(sut.preHandle(request, response, ""));
    }

    //@ParameterizedTest
    @CsvSource({
            "/am/bookings,POST,jbs-create-bookings-api-flag",
            "/am/bookings/query,POST,jbs-query-bookings-api-flag"
    })
    void preHandle_forbiddenError(String url, String method, String flag) {
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(false);
        when(ldClient.isFlagKnown(flag)).thenReturn(true);
        Assertions.assertThrows(ForbiddenException.class, () -> sut.preHandle(request, response, ""));
    }

    //@ParameterizedTest
    @CsvSource({
            "/am/bookings,POST,jbs-create-bookings-api-flag",
            "/am/bookings/query,POST,jbs-query-bookings-api-flag"
    })
    void preHandle_invalidFlag(String url, String method, String flag) {
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(true);
        when(ldClient.isFlagKnown(flag)).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> sut.preHandle(request, response, ""));
    }

    //@ParameterizedTest
    @CsvSource({
            "/am/bookings,,jbs-create-bookings-api-flag",
    })
    void preHandle_flagNull(String url, String method, String flag) {
        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(ldClient.boolVariation(any(), any(), anyBoolean())).thenReturn(true);
        when(ldClient.isFlagKnown(flag)).thenReturn(true);
        Assertions.assertThrows(ForbiddenException.class, () -> sut.preHandle(request, response, ""));
    }

}