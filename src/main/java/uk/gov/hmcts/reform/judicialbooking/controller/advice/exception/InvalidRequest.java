package uk.gov.hmcts.reform.judicialbooking.controller.advice.exception;

public class InvalidRequest extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidRequest(String message) {
        super(message);
    }
}
