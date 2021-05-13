package uk.gov.hmcts.reform.judicialbooking.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;

import java.time.ZonedDateTime;
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
    void shouldValidateDateOrder() {
        ZonedDateTime beginTime = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endTime = ZonedDateTime.now().plusDays(14);
        Assertions.assertDoesNotThrow(() ->
                ValidationUtil.compareDateOrder(beginTime, endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_BeginTimeBeforeCurrent() {
        ZonedDateTime beginTime = ZonedDateTime.now().minusDays(1);
        ZonedDateTime endTime = ZonedDateTime.now().minusDays(2);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeCurrent() {
        ZonedDateTime beginTime = ZonedDateTime.now().plusDays(14);
        ZonedDateTime endTime = ZonedDateTime.now().minusDays(1);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }

    @Test
    void shouldThrow_ValidateDateOrder_EndTimeBeforeBegin() {
        ZonedDateTime beginTime = ZonedDateTime.now().plusDays(14);
        ZonedDateTime endTime = ZonedDateTime.now().plusDays(10);
        Assertions.assertThrows(BadRequestException.class, () ->
                ValidationUtil.compareDateOrder(beginTime,endTime)
        );
    }
}
