package uk.gov.hmcts.reform.judicialbooking.controller.advice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class DuplicateRequestException extends RuntimeException {

    private static final long serialVersionUID = 6L;

    public DuplicateRequestException(String  message) {
        super(message);

    }
}
