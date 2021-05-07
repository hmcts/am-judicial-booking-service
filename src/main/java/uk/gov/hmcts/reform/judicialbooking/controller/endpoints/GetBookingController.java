package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.getbooking.RetrieveBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

@Api(value = "booking")
@RestController
public class GetBookingController {
    private static final Logger logger = LoggerFactory.getLogger(GetBookingController.class);

    private RetrieveBookingOrchestrator retrieveBookingOrchestrator;

    public GetBookingController(RetrieveBookingOrchestrator retrieveBookingOrchestrator) {
        this.retrieveBookingOrchestrator = retrieveBookingOrchestrator;
    }

    @GetMapping(path = "/am/bookings", produces = V1.MediaType.GET_BOOKINGS)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Retrieve JSON representation of bookings for the current actor.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Success",
                    response = BookingsResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = V1.Error.INVALID_REQUEST
            ),
            @ApiResponse(
                    code = 404,
                    message = V1.Error.NO_RECORDS_FOUND_BY_ACTOR
            )
    })
    public ResponseEntity<BookingsResponse> retrieveBookings(
            @RequestHeader(value = "x-correlation-id", required = false) String correlationId) {
        long startTime = System.currentTimeMillis();
        ResponseEntity<BookingsResponse> response = retrieveBookingOrchestrator.getBookings();

        logger.info(" >> retrieveBookings execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }
}