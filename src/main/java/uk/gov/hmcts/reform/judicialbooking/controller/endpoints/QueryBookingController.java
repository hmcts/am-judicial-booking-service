package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.querybookings.QueryBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.transaction.Transactional;

@RestController
@Slf4j
public class QueryBookingController {

    private final QueryBookingOrchestrator queryBookingOrchestrator;

    public QueryBookingController(QueryBookingOrchestrator queryBookingOrchestrator) {
        this.queryBookingOrchestrator = queryBookingOrchestrator;
    }

    @PostMapping(
            path = "/am/bookings/query",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Retrieves bookings based on queried user ID's.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = BookingQueryResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = V1.Error.INVALID_REQUEST
            ),
            @ApiResponse(
                    code = 404,
                    message = V1.Error.NO_RECORDS_FOUND_BY_QUERY
            )
    })
    @Transactional
    public ResponseEntity<BookingQueryResponse> queryBookings(
            @RequestHeader(value = "x-correlation-id", required = false)
                    String correlationId,
            @RequestBody BookingQueryRequest queryRequest) throws Exception {
        long startTime = System.currentTimeMillis();
        ResponseEntity<BookingQueryResponse> response = queryBookingOrchestrator.queryBookings(
                queryRequest);
        log.info(
                " >> queryBookings execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }
}
