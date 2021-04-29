package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.text.ParseException;
import java.time.ZonedDateTime;

@Service
public class ParseRequestService {

    @Autowired
    private SecurityUtils securityUtils;

    public Booking parseBookingRequest(BookingRequest bookingRequest) throws ParseException {
        Booking booking = bookingRequest.getBookingRequestObject();
        booking.setCreated(ZonedDateTime.now());
        booking.setUserId(securityUtils.getUserId());

        return ValidationUtil.validateBookingRequest(booking);
    }

}
