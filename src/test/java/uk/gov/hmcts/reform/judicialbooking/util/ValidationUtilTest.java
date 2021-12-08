package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;

import java.time.LocalDate;

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
