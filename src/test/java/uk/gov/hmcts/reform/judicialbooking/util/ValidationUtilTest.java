package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;

class ValidationUtilTest {

    private final String beginTimeString = "BeginTime";


    @Test
    void shouldValidate() {
        assertTrue(ValidationUtil.validate("1212121212121212"));
        assertFalse(ValidationUtil.validate("2323232323232"));
    }

    @Test
    void shouldValidateInputParams_EmptyString() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateInputParams(INPUT_CASE_ID_PATTERN, "")
        );
    }

    @Test
    void shouldThrow_Pattern_NotMatched() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateInputParams(INPUT_CASE_ID_PATTERN, "myreq@123")
        );
    }

      @Test
      void shouldSanitiseCorrelationId() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.sanitiseCorrelationId("correlation-id-dummy")
                );
      }

    @Test
    void shouldValidateBookingRequest() {
        BookingRequest booking = new BookingRequest();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(booking)
                );
    }

    @Test
    void shouldValidateInputDates() {
        LocalDate beginDate = LocalDate.now().minusYears(51L);
        LocalDate endDate = LocalDate.now().minusYears(51L);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBeginAndEndDates(beginDate,endDate)
                );
    }

    @Test
    void shouldValidateDateOrder() {
        LocalDate beginTime = LocalDate.now().plusDays(1);
        LocalDate endTime = LocalDate.now().plusDays(14);
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_BeginTimeBeforeCurrent() {
        LocalDate beginTime = LocalDate.now().minusDays(1);
        LocalDate endTime = LocalDate.now().minusDays(2);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeCurrent() {
        LocalDate beginTime = LocalDate.now().plusDays(14);
        LocalDate endTime = LocalDate.now().minusDays(1);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeBegin() {
        LocalDate beginTime = LocalDate.now().plusDays(14);
        LocalDate endTime = LocalDate.now().plusDays(10);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }
}
