package uk.gov.hmcts.reform.judicialbooking.controller.advice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final int errorCode;

    private final String errorMessage;

    private final String status;

    private final String timeStamp;

}
