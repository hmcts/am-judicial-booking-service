package uk.gov.hmcts.reform.judicialbooking.util;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class CorrelationInterceptorUtil  {
    //TODO use this
    public String preHandle(final HttpServletRequest request) {
        String correlationId = getCorrelationIdFromHeader(request);
        MDC.put(Constants.CORRELATION_ID_HEADER_NAME, correlationId);
        return correlationId;
    }

    public void afterCompletion() {
        MDC.remove(Constants.CORRELATION_ID_HEADER_NAME);
    }

    private String getCorrelationIdFromHeader(final HttpServletRequest request) {
        String correlationId = "";
        if (ObjectUtils.isEmpty(request.getHeader(Constants.CORRELATION_ID_HEADER_NAME))) {
            correlationId = generateUniqueCorrelationId();
        } else {
            correlationId = request.getHeader(Constants.CORRELATION_ID_HEADER_NAME);
        }
        ValidationUtil.sanitiseCorrelationId(correlationId);
        return correlationId;
    }

    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
