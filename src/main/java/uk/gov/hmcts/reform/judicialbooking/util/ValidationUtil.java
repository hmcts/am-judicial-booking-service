package uk.gov.hmcts.reform.judicialbooking.util;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.INPUT_CASE_ID_PATTERN;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.PredicateValidator.datePredicates;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.PredicateValidator.valuePredicates;

@Named
@Singleton
@Slf4j
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Validate a number string using  algorithm.
     *
     * @param numberString =null
     * @return true if pattern matches
     */
    public static boolean validate(String numberString) {
        validateInputParams(INPUT_CASE_ID_PATTERN, numberString);
        return (numberString != null && numberString.length() == 16);
    }

    public static void validateInputParams(String pattern, String... inputString) {
        for (var input : inputString) {
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
        if (booking == null) {
            throw new BadRequestException("Body cannot be Null or Empty");
        }
        if (datePredicates.test(booking.getBeginDate())) {
            throw new BadRequestException("Begin date cannot be Null or Empty");
        }
        if (datePredicates.test(booking.getEndDate())) {
            throw new BadRequestException("End date cannot be Null or Empty");
        }
        if ((!valuePredicates.test(booking.getLocationId()))
                && (valuePredicates.test(booking.getRegionId()))) {
            throw new BadRequestException("RegionId cannot be Null or Empty, if LocationId is available");
        }
        if (!valuePredicates.test(booking.getUserId())) {
            validateInputParams(Constants.IDAM_ID_PATTERN, booking.getUserId());
        }
        validateBeginAndEndDates(booking.getBeginDate(), booking.getEndDate());
    }

    public static void validateBeginAndEndDates(LocalDate beginDate, LocalDate endDate) {
        if (!datePredicates.test(beginDate) && beginDate.getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for beginDate");
        }
        if (!datePredicates.test(endDate) && endDate.getYear() == 1970) {
            throw new BadRequestException(V1.Error.BAD_REQUEST_INVALID_DATETIME + " for endDate");
        }
        if (!datePredicates.test(beginDate) && !datePredicates.test(endDate)) {
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

    public static void validateUserId(String actualUserId, String expectedUserId) {
        if (!Objects.equals(actualUserId, expectedUserId)) {
            throw new UnprocessableEntityException("The userId is invalid");
        }
    }

}
