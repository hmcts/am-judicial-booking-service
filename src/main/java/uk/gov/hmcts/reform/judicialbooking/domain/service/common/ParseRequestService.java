package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.text.ParseException;
import java.time.ZonedDateTime;

@Service
public class ParseRequestService {

    @Autowired
    private SecurityUtils securityUtils;

    public BookingEntity parseBookingRequest(BookingRequest bookingRequest) throws ParseException {
        BookingEntity booking = bookingRequest.getBookingRequest();
        booking.setCreated(ZonedDateTime.now());
        booking.setUserId(securityUtils.getUserId());

        if (ObjectUtils.isEmpty(booking.getBeginTime())) {
            throw new BadRequestException("Begin time cannot be Null or Empty");
        }
        if (ObjectUtils.isEmpty(booking.getEndTime())) {
            throw new BadRequestException("End time cannot be Null or Empty");
        }

        return ValidationUtil.validateBookingRequest(booking);
    }

}
