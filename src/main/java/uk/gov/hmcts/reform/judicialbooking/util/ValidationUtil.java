package uk.gov.hmcts.reform.judicialbooking.util;

import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

@Named
@Singleton
@Slf4j
public class ValidationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    private ValidationUtil() {
    }

    /**
     * Validate a number string using  algorithm.
     *
     * @param numberString =null
     * @return
     */
    public static boolean validate(String numberString) {
        validateInputParams(INPUT_CASE_ID_PATTERN, numberString);
        return (numberString != null && numberString.length() == 16);
    }

    public static void validateInputParams(String pattern, String... inputString) {
        for (String input : inputString) {
            if (StringUtils.isEmpty(input)) {
                throw new BadRequestException("The input parameter is Null/Empty");
            } else if (!Pattern.matches(pattern, input)) {
                throw new BadRequestException("The input parameter: \"" + input + "\", does not comply with the "
                                              + "required pattern");
            }
        }
    }

    public static void validateLists(List<?>... inputList) {
        for (List<?> list : inputList) {
            if (CollectionUtils.isEmpty(list)) {
                throw new BadRequestException("The List is empty");
            }
        }
    }

    public static boolean sanitiseCorrelationId(String inputString) {
        if (inputString != null && !inputString.isEmpty() && !Pattern.matches(Constants.UUID_PATTERN, inputString)) {
            throw new BadRequestException(
                    String.format(
                            "The input parameter: \"%s\", does not comply with the required pattern",
                            inputString
                    ));
        }
        return true;
    }

    public static BookingEntity validateBookingRequest(BookingEntity booking) throws ParseException {
        validateAppointmentId(booking.getAppointmentId());
        validateBeginAndEndDates(booking);
        return booking;
    }

    public static void validateAppointmentId(String appointmentId) {
        if (appointmentId.isBlank()) {
            throw new BadRequestException("The Booking appointmentId is invalid or missing.");
        }
    }

    public static void validateBeginAndEndDates(BookingEntity booking) {
        if (booking.getBeginTime() != null && booking.getBeginTime().getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for beginTime");
        }
        if (booking.getEndTime() != null && booking.getEndTime().getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for endTime");
        }
        if (booking.getBeginTime() != null && booking.getEndTime() != null) {
            compareDateOrder(
                    booking.getBeginTime(),
                    booking.getEndTime()
            );
        }
    }

    public static void compareDateOrder(ZonedDateTime beginTime, ZonedDateTime endTime) {
        ZonedDateTime createTime = ZonedDateTime.now(ZoneId.of("UTC"));
        if (beginTime.isBefore(createTime)) {
            throw new BadRequestException(
                    String.format("The begin time: %s takes place before the current time: %s",
                            beginTime, createTime
                    ));
        } else if (endTime.isBefore(createTime)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the current time: %s", endTime, createTime));
        } else if (endTime.isBefore(beginTime)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the begin time: %s", endTime, beginTime));
        }
    }
}
