package uk.gov.hmcts.reform.judicialbooking.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.DuplicateRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ForbiddenException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.InvalidRequest;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ControllerAdvice(basePackages = "uk.gov.hmcts.reform.judicialbooking")
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class JudicialBookingControllerAdvice {

    private static final long serialVersionUID = 2L;

    private static final String LOG_STRING = "handling exception: {}";
    private static final Logger logger = LoggerFactory.getLogger(JudicialBookingControllerAdvice.class);

    @ExceptionHandler(InvalidRequest.class)
    public ResponseEntity<Object> customValidationError(
        InvalidRequest ex) {
        return errorDetailsResponseEntity(
            ex,
            BAD_REQUEST,
            ErrorConstants.INVALID_REQUEST.getErrorCode(),
            ErrorConstants.INVALID_REQUEST.getErrorMessage()
                                         );
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<Object> handleHttpMessageConversionException(
            HttpServletRequest request,
            HttpMessageConversionException exception) {
        return errorDetailsResponseEntity(
                exception,
                BAD_REQUEST,
                ErrorConstants.BAD_REQUEST.getErrorCode(),
                ErrorConstants.INVALID_REQUEST.getErrorMessage()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(
            HttpServletRequest request,
            BadRequestException exception) {
        return errorDetailsResponseEntity(
                exception,
                BAD_REQUEST,
                ErrorConstants.BAD_REQUEST.getErrorCode(),
                ErrorConstants.BAD_REQUEST.getErrorMessage());
    }

    @ExceptionHandler(DuplicateRequestException.class)
    protected ResponseEntity<Object> handleDuplicateRequestException(
            HttpServletRequest request,
            DuplicateRequestException exception) {
        return errorDetailsResponseEntity(
                exception,
                BAD_REQUEST,
                ErrorConstants.BAD_REQUEST.getErrorCode(),
                ErrorConstants.BAD_REQUEST.getErrorMessage());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<Object> handleUnprocessableEntityException(
            HttpServletRequest request,
            UnprocessableEntityException exception) {
        return errorDetailsResponseEntity(
                exception,
                HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorConstants.UNPROCESSABLE_ENTITY.getErrorCode(),
                ErrorConstants.UNPROCESSABLE_ENTITY.getErrorMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(
            HttpServletRequest request,
            ResourceNotFoundException exception) {
        return errorDetailsResponseEntity(
                exception,
                HttpStatus.NOT_FOUND,
                ErrorConstants.NOT_FOUND.getErrorCode(),
                ErrorConstants.NOT_FOUND.getErrorMessage()
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> customForbiddenException(
            ForbiddenException ex) {
        return errorDetailsResponseEntity(
                ex,
                HttpStatus.FORBIDDEN,
                ErrorConstants.ACCESS_DENIED.getErrorCode(),
                ErrorConstants.ACCESS_DENIED.getErrorMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(
        HttpServletRequest request,
        Exception exception) {
        return errorDetailsResponseEntity(
            exception,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorConstants.UNKNOWN_EXCEPTION.getErrorCode(),
            ErrorConstants.UNKNOWN_EXCEPTION.getErrorMessage());
    }

    public String getTimeStamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.ENGLISH).format(new Date());
    }

    public static Throwable getRootException(Throwable exception) {
        Throwable rootException = exception;
        while (rootException.getCause() != null) {
            rootException = rootException.getCause();
        }
        return rootException;
    }

    private ResponseEntity<Object> errorDetailsResponseEntity(Exception ex, HttpStatus httpStatus, int errorCode,
                                                              String errorMsg) {

        logger.error(LOG_STRING, ex);
        ErrorResponse errorDetails = ErrorResponse.builder()
                                                  .errorCode(errorCode)
                                                  .errorMessage(errorMsg)
                                                  .errorDescription(getRootException(ex).getLocalizedMessage())
                                                  .timeStamp(getTimeStamp())
                                                  .build();
        return new ResponseEntity<>(
            errorDetails, httpStatus);
    }
}
