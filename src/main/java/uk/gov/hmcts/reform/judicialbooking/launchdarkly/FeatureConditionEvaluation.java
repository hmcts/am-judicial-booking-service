package uk.gov.hmcts.reform.judicialbooking.launchdarkly;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import com.launchdarkly.shaded.org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ForbiddenException;
import uk.gov.hmcts.reform.judicialbooking.controller.advice.exception.ResourceNotFoundException;
import uk.gov.hmcts.reform.judicialbooking.util.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
public class FeatureConditionEvaluation implements HandlerInterceptor {

    public static final String USER = "user";
    public static final String SERVICE_NAME = "servicename";
    public static final String POST = "POST";

    private SecurityUtils securityUtils;

    private LDClientInterface ldClient;

    @Value("${launchdarkly.sdk.environment}")
    private String environment;

    @Value("${launchdarkly.sdk.user}")
    private String userName;
    private static final HashMap<String, String> postRequestMap = new HashMap<>();

    static {
        //Any new end point need to be placed in respective map.
        postRequestMap.put("/am/bookings","jbs-create-bookings-api-flag");
        postRequestMap.put("/am/bookings/query","jbs-query-bookings-api-flag");
    }

    @Autowired
    public FeatureConditionEvaluation(@Lazy SecurityUtils securityUtils, LDClientInterface ldClient) {
        this.securityUtils = securityUtils;
        this.ldClient = ldClient;
    }

    //@Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object arg2) {

        var flagName = getLaunchDarklyFlag(request);

        if (flagName == null) {
            throw new ForbiddenException("The endpoint is not configured in Launch Darkly");
        }

        if (!isValidFlag(flagName)) {
            throw new ResourceNotFoundException(String.format(
                    "The flag %s is not configured in Launch Darkly", flagName));
        }

        var flagStatus = isFlagEnabled(securityUtils.getServiceName(), flagName);
        if (!flagStatus) {
            throw new ForbiddenException(String.format("Launch Darkly flag is not enabled for the endpoint %s",
                    request.getRequestURI()));
        } else {
            return true;
        }
    }

    public boolean isFlagEnabled(String serviceName, String flagName) {
        LDUser user = new LDUser.Builder(environment)
                .firstName(userName)
                .lastName(USER)
                .custom(SERVICE_NAME, serviceName)
                .build();

        return ldClient.boolVariation(flagName, user, false);
    }

    public boolean isValidFlag(String flagName) {
        return ldClient.isFlagKnown(flagName);
    }

    public String getLaunchDarklyFlag(HttpServletRequest request) {
        var uri = request.getRequestURI();
        if (POST.equals(request.getMethod()) && postRequestMap.get(uri) != null) {
            return postRequestMap.get(uri);
        } else {
            return null;
        }
    }
}