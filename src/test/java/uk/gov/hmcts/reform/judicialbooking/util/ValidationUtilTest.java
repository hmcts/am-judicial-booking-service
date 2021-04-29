package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationUtilTest {

    private final String beginTime = "BeginTime";

    @Test
    void shouldValidate() {
        assertEquals(true, ValidationUtil.validate("1212121212121212"));
        assertEquals(false, ValidationUtil.validate("2323232323232"));
    }

    @Test
    void shouldThrowBadRequestException_ValidateLists() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            ValidationUtil.validateLists(new ArrayList());
        });
    }

    @Test
    void shouldValidateTTL() {
        assertEquals(false, ValidationUtil.validateTTL("2021-12-31T10:10:10+"));
        assertEquals(false, ValidationUtil.validateTTL("2021-12-31T10:10:10+9999"));
        assertEquals(false, ValidationUtil.validateTTL("2021-12-31T10:10:10+999Z"));
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
                ValidationUtil.validateDateTime(LocalDateTime.now().plusMinutes(1).toString(), beginTime)
        );
    }

    @Test
    void validateDateTime_ThrowLessThanLimit() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateDateTime("2050-09-01T00:", beginTime)
        );
    }

    @Test
    void validateDateTime_ThrowParseException() {
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.validateDateTime("2050-090000000000000", beginTime)
        );
    }
}
