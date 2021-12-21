package uk.gov.hmcts.reform.judicialbooking.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDate;
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

    public static boolean sanitiseUuid(String inputString) {
        if (inputString != null && !inputString.isEmpty() && !Pattern.matches(Constants.UUID_PATTERN, inputString)) {
            throw new BadRequestException(
                    String.format(
                            "The input parameter: \"%s\", does not comply with the required pattern",
                            inputString
                    ));
        }
        return true;
    }

    public static void validateBookingRequest(BookingRequest booking) {
        if (ObjectUtils.isEmpty(booking.getBeginDate())) {
            throw new BadRequestException("Begin date cannot be Null or Empty");
        }
        if (ObjectUtils.isEmpty(booking.getEndDate())) {
            throw new BadRequestException("End date cannot be Null or Empty");
        }
        if ((!ObjectUtils.isEmpty(booking.getLocationId()))
                && (ObjectUtils.isEmpty(booking.getRegionId()))) {
            throw new BadRequestException("RegionId cannot be Null or Empty, if LocationId is available");
        }
        if (!ObjectUtils.isEmpty(booking.getUserId())) {
            validateInputParams(Constants.NUMBER_TEXT_HYPHEN_PATTERN, booking.getUserId());
        }
        validateBeginAndEndDates(booking.getBeginDate(), booking.getEndDate());
    }

    public static void validateBeginAndEndDates(LocalDate beginDate, LocalDate endDate) {
        if (beginDate != null && beginDate.getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for beginDate");
        }
        if (endDate != null && endDate.getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for endDate");
        }
        if (beginDate != null && endDate != null) {
            compareDateOrder(beginDate, endDate);
        }
    }

    public static void compareDateOrder(LocalDate beginTime, LocalDate endTime) {
        if (beginTime.isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    String.format("The begin time: %s takes place before the current time: %s",
                            beginTime, LocalDate.now()
                    ));
        } else if (endTime.isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the current time: %s",
                            endTime, LocalDate.now()));
        } else if (endTime.isBefore(beginTime)) {
            throw new BadRequestException(
                    String.format("The end time: %s takes place before the begin time: %s", endTime, beginTime));
        }
    }
}
