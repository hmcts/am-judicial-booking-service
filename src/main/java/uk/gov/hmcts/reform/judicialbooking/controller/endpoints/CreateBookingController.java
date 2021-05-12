package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.createbooking.CreateBookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.transaction.Transactional;


@Api(value = "booking")
@RestController
@Slf4j
public class CreateBookingController {

    private final CreateBookingOrchestrator createBookingOrchestrator;

    public CreateBookingController(CreateBookingOrchestrator createBookingOrchestrator) {
        this.createBookingOrchestrator = createBookingOrchestrator;
    }

    @PostMapping(
            path = "/am/bookings",
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
        ResponseEntity<BookingResponse> response = createBookingOrchestrator
                .createBooking(bookingRequest);
        log.info(
                " >> createBooking execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }
}
