package uk.gov.hmcts.reform.judicialbooking.controller.advice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.gov.hmcts.reform.judicialbooking.apihelper.Constants;

import java.util.UUID;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException  extends RuntimeException {

    private static final long serialVersionUID = 7L;

    public ForbiddenException(String message) {
        super(String.format(Constants.FORBIDDEN + ": %s", message));
    }

    public ForbiddenException(UUID message) {
        super(message.toString());
    }
}
