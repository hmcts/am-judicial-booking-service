package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil.validateBookingRequest;
import static uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil.validateInputParams;
import static uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil.validateUserId;

@Service
public class ParseRequestService {

    private final SecurityUtils securityUtils;

    private final  List<String> byPassQueryValidationForServices;


    public ParseRequestService(SecurityUtils securityUtils,
                               @Value("${judicial-booking.query.bypass-userid-validation-for-services}")
                               final String byPassQueryValidationForServices){
        this.byPassQueryValidationForServices = List.of(byPassQueryValidationForServices.split(","));
        this.securityUtils = securityUtils;
    }


    public BookingEntity parseBookingRequest(BookingRequest bookingRequest) {
        validateBookingRequest(bookingRequest);
        if (StringUtils.isEmpty(bookingRequest.getUserId())) {
            bookingRequest.setUserId(securityUtils.getUserId());
        } else {
            validateUserId(bookingRequest.getUserId(), securityUtils.getUserId());
        }

        return BookingEntity.builder()
                .created(ZonedDateTime.now())
                .userId(bookingRequest.getUserId())
                .beginTime(bookingRequest.getBeginDate().atStartOfDay(ZoneId.of("UTC")))
                .endTime(bookingRequest.getEndDate().plusDays(1).atStartOfDay(ZoneId.of("UTC")))
                .locationId(bookingRequest.getLocationId())
                .regionId(bookingRequest.getRegionId())
                .log("Booking record is successfully created")
                .build();
    }

    public List<String> parseQueryRequest(BookingQueryRequest queryRequest) {
        if (queryRequest.getQueryRequest() == null
                || CollectionUtils.isEmpty(queryRequest.getQueryRequest().getUserIds())) {
            throw new BadRequestException("Provided list of userIds is empty");
        }

        validateInputParams(Constants.IDAM_ID_PATTERN,
                queryRequest.getQueryRequest().getUserIds().toArray(new String[0]));

        //NBP Fix for AM-2911
        String serviceName = securityUtils.getServiceName();
        if (Objects.nonNull(serviceName) && !byPassQueryValidationForServices.contains(serviceName) &&
                queryRequest.getQueryRequest().getUserIds().size() == 1) {
            validateUserId(queryRequest.getQueryRequest().getUserIds().get(0), securityUtils.getUserId());
        }

        return queryRequest.getQueryRequest().getUserIds();
    }

}
