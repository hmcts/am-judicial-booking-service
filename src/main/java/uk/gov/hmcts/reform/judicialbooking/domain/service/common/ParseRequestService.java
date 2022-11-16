package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.util.Constants;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;
import uk.gov.hmcts.reform.judicialbooking.util.ValidationUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ParseRequestService {

    @Autowired
    private SecurityUtils securityUtils;

    public BookingEntity parseBookingRequest(BookingRequest bookingRequest) {
        ValidationUtil.validateBookingRequest(bookingRequest);
        String userId = StringUtils.isEmpty(bookingRequest.getUserId()) ? securityUtils.getUserId() :
                bookingRequest.getUserId();
        checkUserId(userId);
        return BookingEntity.builder()
                .created(ZonedDateTime.now())
                .userId(userId)
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
        } else if (queryRequest.getQueryRequest().getUserIds().size() == 1) {
            checkUserId(queryRequest.getQueryRequest().getUserIds().get(0));
        }

        ValidationUtil.validateInputParams(Constants.UUID_PATTERN,
                queryRequest.getQueryRequest().getUserIds().toArray(new String[0]));

        return queryRequest.getQueryRequest().getUserIds();
    }

    public void checkUserId(String userId) {
        if (!Objects.equals(securityUtils.getUserId(), userId)) {
            throw new UnprocessableEntityException("The userId is invalid");
        }
    }


}
