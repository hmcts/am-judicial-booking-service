package uk.gov.hmcts.reform.judicialbooking.controller;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.judicialbooking.domain.service.BookingOrchestrator;
import uk.gov.hmcts.reform.judicialbooking.v1.V1;

@RestController
public class WelcomeController {

    @Autowired
    BookingOrchestrator bookingOrchestrator;

    @GetMapping(value = "/swagger")
    public String index() {
        return "redirect:swagger-ui.html";
    }


    @GetMapping(value = "/welcome")
    public String welcome() {
        return " Welcome to Judicial Booking service";
    }


    @DeleteMapping(
            path = "am/bookings/{userId}",
            produces = V1.MediaType.DELETE_BOOKINGS
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "No Content"
            ),
            @ApiResponse(
                    code = 400,
                    message = V1.Error.INVALID_REQUEST
            )
    })
    public ResponseEntity<Void> deleteBookingByUserId(@ApiParam(value = "userId", required = true)
                                                         @PathVariable String userId) {
        return bookingOrchestrator.deleteBookingByUserId(userId);
    }
}
