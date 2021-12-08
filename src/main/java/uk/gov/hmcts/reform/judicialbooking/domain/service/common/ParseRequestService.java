package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ParseRequestService {

    @Autowired
    private SecurityUtils securityUtils;

    public BookingEntity parseBookingRequest(BookingRequest bookingRequest) throws ParseException {
        BookingEntity booking = bookingRequest.getBookingRequest();
        booking.setCreated(ZonedDateTime.now());
        booking.setUserId(securityUtils.getUserId());

        if (ObjectUtils.isEmpty(booking.getBeginDate())) {
            throw new BadRequestException("Begin date cannot be Null or Empty");
        }
        if (ObjectUtils.isEmpty(booking.getEndDate())) {
            throw new BadRequestException("End date cannot be Null or Empty");
        } else {
            booking.setEndDate(booking.getEndDate().plusDays(1L).toLocalDate());
        }

        if ((!ObjectUtils.isEmpty(booking.getLocationId()))
                && (ObjectUtils.isEmpty(booking.getRegionId()))) {
            throw new BadRequestException("RegionId cannot be Null or Empty, if LocationId is available");
        }

        return ValidationUtil.validateBookingRequest(booking);
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
