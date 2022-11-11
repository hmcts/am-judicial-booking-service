package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;


class ValidationUtilTest {

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
                ValidationUtil.sanitiseUuid("correlation-id-dummy")
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
                ValidationUtil.validateBeginAndEndDates(beginDate, endDate)
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
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeCurrent() {
        LocalDate beginTime = LocalDate.now().plusDays(14);
        LocalDate endTime = LocalDate.now().minusDays(1);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeBegin() {
        LocalDate beginTime = LocalDate.now().plusDays(14);
        LocalDate endTime = LocalDate.now().plusDays(10);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void sanitizeCorrelationIdReturnsTrue() {
        final String uuid = UUID.randomUUID().toString();
        final Pattern pattern = Pattern.compile(Constants.UUID_PATTERN);
        final Matcher matcher = pattern.matcher(uuid);
        assertTrue(matcher.matches());
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        final boolean result = ValidationUtil.sanitiseUuid(uuid);
        assertTrue(result);

    }

    @Test
    void sanitizeCorrelationIdThrowsException() {
        final String inputString = "HelloClarice";
        final Pattern pattern = Pattern.compile(Constants.UUID_PATTERN);
        final Matcher matcher = pattern.matcher(inputString);
        assertFalse(matcher.matches());
        assertNotNull(inputString);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.sanitiseUuid(inputString)
        );
    }

    @Test
    void validateBookingRequest_happy() {
        BookingRequest bookingRequest = BookingRequest.builder().regionId("BA1").locationId("south-east")
                .beginDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusYears(1))
                .build();
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBookingRequest(bookingRequest)
        );
    }

    @Test
    void validateBookingRequest_nullBeginDate() {
        BookingRequest bookingRequest = BookingRequest.builder().regionId("BA1").locationId("south-east")
                .beginDate(null)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(bookingRequest)
        );
    }

    @Test
    void validateBookingRequest_null() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(null)
        );
    }

    @Test
    void validateBookingRequest_nullEndDate() {
        BookingRequest bookingRequest = BookingRequest.builder().regionId("BA1").locationId("south-east")
                .beginDate(LocalDate.now().plusDays(1))
                .endDate(null)
                .build();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(bookingRequest)
        );
    }

    @Test
    void validateBookingRequest_regionIdNull() {
        BookingRequest bookingRequest = BookingRequest.builder().regionId(null).locationId("south-east")
                .beginDate(null)
                .endDate(LocalDate.now().plusYears(1))
                .build();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(bookingRequest)
        );
    }

    @Test
    void validateDates_beginYear_Happy() {
        LocalDate beginDate = LocalDate.now().plusDays(5);
        LocalDate endDate = LocalDate.now().plusYears(2);
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBeginAndEndDates(beginDate, endDate)
        );
    }

    @Test
    void validateDates_beginYear1970() {
        LocalDate beginDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now().plusYears(3);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBeginAndEndDates(beginDate, endDate)
        );
    }

    @Test
    void validateDates_endYear1970() {
        LocalDate beginDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.of(1970, 1, 1);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBeginAndEndDates(beginDate, endDate)
        );
    }

    @Test
    void validateDates_beginYearNull() {
        LocalDate endDate = LocalDate.now().plusYears(1);
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBeginAndEndDates(null, endDate)
        );
    }

    @Test
    void validateDates_endYearNull() {
        LocalDate beginDate = LocalDate.of(2022, 1, 1);
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBeginAndEndDates(beginDate, null)
        );
    }

    @Test
    void validateDates_now() {
        LocalDate endDate = LocalDate.now();
        LocalDate beginDate = LocalDate.now();
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBeginAndEndDates(beginDate, endDate)
        );
    }

    @Test
    void validateDates_null() {
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateBeginAndEndDates(null, null)
        );
    }

    @Test
    void sanitiseUuid_null() {
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.sanitiseUuid(null)
        );
    }

    @Test
    void sanitiseUuid_empty() {
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.sanitiseUuid("")
        );
    }

    @Test
    void validateBookingRequest_withoutRegionId() {
        BookingRequest bookingRequest = BookingRequest.builder().locationId("south-east")
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .build();
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateBookingRequest(bookingRequest)
        );
        Assertions.assertTrue(exception.getLocalizedMessage().contains("RegionId cannot be Null or Empty, "
                + "if LocationId is available"));
    }

    @Test
    void validateBookingRequest_withOnlyRegionId() {
        BookingRequest bookingRequest = BookingRequest.builder().regionId("south-east")
                .beginDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .build();
        Assertions.assertDoesNotThrow(() -> ValidationUtil.validateBookingRequest(bookingRequest));

    }
}
