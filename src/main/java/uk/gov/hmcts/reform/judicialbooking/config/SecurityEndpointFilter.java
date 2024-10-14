package uk.gov.hmcts.reform.judicialbooking.config;

import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityEndpointFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            if (StringUtils.isNotEmpty(request.getRequestURI()) && request.getRequestURI().contains("/am")) {
                logger.debug("The Service Auth token length is: "
                        + request.getHeader("ServiceAuthorization").length());
                logger.debug("The User Auth token length : "
                        + request.getHeader("Authorization").length());
                logger.debug("The User Auth token contains 'Bearer '? : "
                        + request.getHeader("Authorization").contains("Bearer "));
            }
        } catch (Exception e) {
            logger.info("Exception while processing the user or service tokens : " + e.getMessage());
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof FeignException.FeignClientException) {
                FeignException.FeignClientException feignClientException =
                    (FeignException.FeignClientException) throwable;
                response.setStatus(feignClientException.status());
                return;
            }
            throw e;
        }
    }
}
