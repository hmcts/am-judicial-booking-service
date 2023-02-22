package uk.gov.hmcts.reform.judicialbooking.controller.endpoints;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequestWrapper;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import javax.transaction.Transactional;


@RestController
@Slf4j
public class CreateBookingController {

    private final BookingOrchestrator bookingOrchestrator;

    public CreateBookingController(BookingOrchestrator bookingOrchestrator) {
        this.bookingOrchestrator = bookingOrchestrator;
    }

    @PostMapping(
            path = "/am/bookings",
            produces = V1.MediaType.CREATE_BOOKING,
            consumes = {"application/json"}
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates booking for a fee-pay judge",
            security =
                    {
                            @SecurityRequirement(name = "Authorization"),
                            @SecurityRequirement(name = "ServiceAuthorization")
                    })
    @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(schema = @Schema(implementation = BookingResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = V1.Error.INVALID_REQUEST
    )
    @Transactional
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader(value = "x-correlation-id", required = false)
                    String correlationId,
            @Validated
            @RequestBody BookingRequestWrapper bookingRequest) {
        var startTime = System.currentTimeMillis();
        ResponseEntity<BookingResponse> response = bookingOrchestrator
                .createBooking(bookingRequest.getBookingRequest());
        log.info(
                " >> createBooking execution finished at {} . Time taken = {} milliseconds",
                System.currentTimeMillis(),
                Math.subtractExact(System.currentTimeMillis(), startTime)
        );
        return response;
    }
}
