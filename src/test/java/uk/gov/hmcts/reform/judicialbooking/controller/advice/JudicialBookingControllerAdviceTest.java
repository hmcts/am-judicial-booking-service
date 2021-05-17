package uk.gov.hmcts.reform.judicialbooking.controller.advice;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.BadRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.DuplicateRequestException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.InvalidRequest;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.UnprocessableEntityException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

class JudicialBookingControllerAdviceTest {

    private final JudicialBookingControllerAdvice csda = new JudicialBookingControllerAdvice();
    private final HttpServletRequest servletRequestMock = mock(HttpServletRequest.class);

    @Test
    void customValidationError() {
        InvalidRequest invalidRequestException = mock(InvalidRequest.class);
        ResponseEntity<Object> responseEntity = csda.customValidationError(invalidRequestException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void handleHttpMessageConversionException() {
        HttpMessageConversionException httpMessageConversionException = mock(HttpMessageConversionException.class);
        ResponseEntity<Object> responseEntity = csda.handleHttpMessageConversionException(
            servletRequestMock, httpMessageConversionException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void handleUnknownException() {
        Exception exception = mock(Exception.class);
        ResponseEntity<Object> responseEntity = csda.handleUnknownException(servletRequestMock, exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());

    }

    @Test
    void getTimeStamp() {
        String time = csda.getTimeStamp();
        assertEquals(time.substring(0,16), new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).format(new Date()));
    }

    @Test
    void handleBadRequestException() {
        BadRequestException exception = mock(BadRequestException.class);
        ResponseEntity<Object> responseEntity = csda.handleBadRequestException(servletRequestMock, exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());

    }

    @Test
    void handleUnprocessableEntityException() {
        UnprocessableEntityException exception = mock(UnprocessableEntityException.class);
        ResponseEntity<Object> responseEntity = csda.handleUnprocessableEntityException(servletRequestMock, exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void handleDduplicateRequestException() {
        DuplicateRequestException exception = mock(DuplicateRequestException.class);
        ResponseEntity<Object> responseEntity = csda.handleDuplicateRequestException(servletRequestMock, exception);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());

    }
}
