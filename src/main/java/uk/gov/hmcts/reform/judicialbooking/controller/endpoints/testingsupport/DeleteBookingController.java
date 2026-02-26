package uk.gov.hmcts.reform.judicialbooking.controller.endpoints.testingsupport;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.AUTHORIZATION;
import static uk.gov.hmcts.reform.judicialbooking.apihelper.Constants.SERVICE_AUTHORIZATION2;

@RestController
@Slf4j
@NoArgsConstructor
@ConditionalOnProperty(name = "testing.support.enabled", havingValue = "true")
public class DeleteBookingController {

    private BookingOrchestrator bookingOrchestrator;

    @Autowired
    public DeleteBookingController(BookingOrchestrator bookingOrchestrator) {
        this.bookingOrchestrator = bookingOrchestrator;
    }

    @DeleteMapping(
            path = "/am/testing-support/bookings/{userId}",
            produces = V1.MediaType.DELETE_BOOKINGS
    )
    @Operation(summary = "deletes a booking by user",
        security =
        {
            @SecurityRequirement(name = AUTHORIZATION),
            @SecurityRequirement(name = SERVICE_AUTHORIZATION2)
        })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @ApiResponse(
            responseCode = "400",
            description = V1.Error.INVALID_REQUEST
    )
    public ResponseEntity<Void> deleteBookingByUserId(@Parameter(description = "userId", required = true)
                                                      @PathVariable String userId) {
        return bookingOrchestrator.deleteBookingByUserId(userId);
    }

    /**
     * This method is deprecated and should not be used.
     * @deprecated Use {@link #deleteBookingByUserId(String)} instead.
     */
    @Deprecated
    @DeleteMapping(
            path = "am/bookings/{userId}",
            produces = V1.MediaType.DELETE_BOOKINGS
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Hidden
    public ResponseEntity<Void> deleteBookingByUserIdDeprecated(
            @Parameter(description = "userId", required = true)
            @PathVariable String userId) {
        log.info("deleteBookingByUserIdDeprecated called");
        return deleteBookingByUserId(userId);
    }
}
