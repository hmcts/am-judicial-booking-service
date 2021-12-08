package uk.gov.hmcts.reform.judicialbooking.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.inject.Named;
import javax.inject.Singleton;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Pattern;

import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;

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
        validateBeginAndEndDates(booking);
        return booking;
    }

    public static void validateAppointmentId(String appointmentId) {
        if (appointmentId.isBlank()) {
            throw new BadRequestException("The Booking appointmentId is invalid or missing.");
        }
    }

    public static void validateBeginAndEndDates(BookingEntity booking) {
        if (booking.getBeginDate() != null && booking.getBeginDate().getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for beginDate");
        }
        if (booking.getEndDate() != null && booking.getEndDate().getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for endDate");
        }
        if (booking.getBeginDate() != null && booking.getEndDate() != null) {
            compareDateOrder(
                    booking.getBeginDate(),
                    booking.getEndDate()
            );
        }
    }

    public static void compareDateOrder(ZonedDateTime beginTime, ZonedDateTime endTime) {
        if (beginTime.isBefore(ZonedDateTime.now())) {
            throw new BadRequestException(
                    String.format("The begin time: %s takes place before the current time: %s",
                            beginTime, LocalDate.now()
                    ));
        } else if (endTime.isBefore(ZonedDateTime.now())) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the current time: %s",
                            endTime, LocalDate.now()));
        } else if (endTime.isBefore(beginTime)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the begin time: %s", endTime, beginTime));
        }
    }
}
