package uk.gov.hmcts.reform.judicialbooking.util;

import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
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
            if (ObjectUtils.isEmpty(input)) {
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

    public static boolean validateTTL(String strDate) {
        if (strDate.length() < 24) {
            return false;
        }
        String timeZone = strDate.substring(20);

        if (timeZone.chars().allMatch(Character::isDigit)) {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            sdfrmt.setLenient(false);
            try {
                Date javaDate = sdfrmt.parse(strDate);
                LOG.info("TTL {}", javaDate);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
        return false;
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

    public static boolean validateAppointmentId(String appointmentId) {
        if (appointmentId.isBlank()) {
            throw new BadRequestException("The Booking appointmentId is invalid or missing.");
        } else {
            return true;
        }
    }

    public static void validateBeginAndEndDates(BookingEntity booking) throws ParseException {
        if (booking.getBeginTime() != null) {
            if (booking.getBeginTime().getYear() == 1970) {
                throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for beginTime");
            }
            validateDateTime(booking.getBeginTime().toString(), "BeginTime");
        }
        if (booking.getEndTime() != null) {
            if (booking.getEndTime().getYear() == 1970) {
                throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for endTime");
            }
            validateDateTime(booking.getEndTime().toString(), "EndTime");
        }
        if (booking.getBeginTime() != null && booking.getEndTime() != null) {
            compareDateOrder(
                    booking.getBeginTime().toString(),
                    booking.getEndTime().toString()
            );
        }
    }

    public static void validateDateTime(String strDate, String timeParam) {
        LOG.info("validateDateTime");
        if (strDate.length() < 16) {
            throw new BadRequestException(String.format(
                    "Incorrect date format %s",
                    strDate
            ));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
        simpleDateFormat.setLenient(false);
        Date javaDate;
        try {
            javaDate = simpleDateFormat.parse(strDate);
            if (LOG.isInfoEnabled() && javaDate != null) {
                LOG.info(javaDate.toString());
            }
        } catch (ParseException e) {
            throw new BadRequestException(String.format(
                    "Incorrect date format %s",
                    strDate
            ));
        }
        assert javaDate != null;
        if (javaDate.before(new Date())) {
            throw new BadRequestException(String.format(
                    "The parameter '%s' cannot be prior to current date", timeParam
            ));
        }
    }

    public static void compareDateOrder(String beginTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_PATTERN);
        Date beginTimeP = sdf.parse(beginTime);
        Date endTimeP = sdf.parse(endTime);
        Date createTimeP = new Date();

        if (beginTimeP.before(createTimeP)) {
            throw new BadRequestException(
                    String.format("The begin time: %s takes place before the current time: %s",
                            beginTime, createTimeP
                    ));
        } else if (endTimeP.before(createTimeP)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the current time: %s", endTime, createTimeP));
        } else if (endTimeP.before(beginTimeP)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the begin time: %s", endTime, beginTime));
        }
    }
}
