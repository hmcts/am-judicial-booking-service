package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    private final String beginTimeString = "BeginTime";

    @Test
    void shouldValidate() {
        assertTrue(ValidationUtil.validate("1212121212121212"));
        assertFalse(ValidationUtil.validate("2323232323232"));
    }

    @Test
    void shouldThrowBadRequestException_ValidateLists() {
        List<Object> list = Collections.emptyList();
        Assertions.assertThrows(BadRequestException.class, () -> {
            ValidationUtil.validateLists(list);
        });
    }

    @Test
    void shouldValidateTTL() {
        assertFalse(ValidationUtil.validateTTL("2021-12-31T10:10:10+"));
        assertFalse(ValidationUtil.validateTTL("2021-12-31T10:10:10+9999"));
        assertFalse(ValidationUtil.validateTTL("2021-12-31T10:10:10+999Z"));
    }

    @Test
    void shouldValidateDateOrder() {
        String beginTime = LocalDateTime.now().plusDays(1).toString();
        String endTime = LocalDateTime.now().plusDays(14).toString();
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_BeginTimeBeforeCurrent() {
        String beginTime = LocalDateTime.now().minusDays(1).toString();
        String endTime = LocalDateTime.now().minusDays(2).toString();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeCurrent() {
        String beginTime = LocalDateTime.now().plusDays(14).toString();
        String endTime = LocalDateTime.now().minusDays(1).toString();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeBegin() {
        String beginTime = LocalDateTime.now().plusDays(14).toString();
        String endTime = LocalDateTime.now().plusDays(10).toString();
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void should_validateDateTime() {
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.validateDateTime(LocalDateTime.now().plusMinutes(1).toString(), beginTimeString)
        );
    }

    @Test
    void validateDateTime_ThrowLessThanLimit() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateDateTime("2050-09-01T00:", beginTimeString)
        );
    }

    @Test
    void validateDateTime_ThrowParseException() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateDateTime("2050-090000000000000", beginTimeString)
        );
    }
}
