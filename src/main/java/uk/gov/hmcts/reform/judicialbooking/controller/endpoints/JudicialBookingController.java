package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingsResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.transaction.Transactional;


@Api(value = "booking")
@RequestMapping(path = "/am/bookings")
@RestController
@Slf4j
public class JudicialBookingController {

    private final BookingOrchestrator bookingOrchestrator;

    public JudicialBookingController(BookingOrchestrator createBookingOrchestrator) {
        this.bookingOrchestrator = createBookingOrchestrator;
    }

    @PostMapping(
            produces = V1.MediaType.CREATE_BOOKING,
            consumes = {"application/json"}
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation("creates bookings and associated assignments")
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Created",
                    response = BookingResponse.class
            ),
            @ApiResponse(
                    code = 400,
                    message = V1.Error.INVALID_REQUEST
            )
    })
    @Transactional
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader(value = "x-correlation-id", required = false)
                    String correlationId,
            @Validated
            @RequestBody BookingRequest bookingRequest) throws Exception {
        long startTime = System.currentTimeMillis();
        ResponseEntity<BookingResponse> response = bookingOrchestrator.createBooking(bookingRequest);
        log.info(
                " >> createBooking execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }

    @GetMapping(produces = V1.MediaType.GET_BOOKINGS)
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
        ResponseEntity<BookingsResponse> response = bookingOrchestrator.getBookings();

        log.info(" >> retrieveBookings execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }
}
