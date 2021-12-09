package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ParseRequestService {

    @Autowired
    private SecurityUtils securityUtils;

    public BookingEntity parseBookingRequest(BookingRequest bookingRequest) throws ParseException {
        ValidationUtil.validateBookingRequest(bookingRequest);
        BookingEntity booking = BookingEntity.builder()
                .created(ZonedDateTime.now())
                .userId(securityUtils.getUserId())
                .beginTime(bookingRequest.getBeginDate().atStartOfDay(ZoneId.of("UTC")))
                .endTime(bookingRequest.getEndDate().plusDays(1).atStartOfDay(ZoneId.of("UTC")))
                .locationId(bookingRequest.getLocationId())
                .regionId(bookingRequest.getRegionId())
                .log("Booking record is successfully created")
                .build();
        return booking;
    }

    public List<String> parseQueryRequest(BookingQueryRequest queryRequest) {
        if (queryRequest.getQueryRequest() == null
                || CollectionUtils.isEmpty(queryRequest.getQueryRequest().getUserIds())) {
            throw new BadRequestException("Provided list of userIds is empty");
        }

        ValidationUtil.validateInputParams(Constants.UUID_PATTERN,
                queryRequest.getQueryRequest().getUserIds().toArray(new String[0]));

        return queryRequest.getQueryRequest().getUserIds();
    }

}
