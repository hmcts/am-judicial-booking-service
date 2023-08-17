package uk.gov.hmcts.reform.judicialbooking.util;

public final class Constants {

    private Constants() {
    }

    public static final String BAD_REQUEST = "Bad Request";
    public static final String FORBIDDEN = "Forbidden: Insufficient permissions";
    public static final String SERVICE_AUTHORIZATION = "serviceauthorization";
    public static final String BEARER = "Bearer ";
    public static final String PROD = "PROD";

    public static final String NUMBER_PATTERN = "^[0-9]{16}$";
    public static final String NUMBER_TEXT_PATTERN = "^[a-zA-Z0-9]+$";
    public static final String TEXT_HYPHEN_PATTERN = "^[-a-zA-Z]*$";
    public static final String NUMBER_TEXT_HYPHEN_PATTERN = "^[-a-zA-Z0-9]*$";
    public static final String TEXT_PATTERN = "^[a-zA-Z]*$";

    public static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-"
            + "[089ab][0-9a-f]{3}-[0-9a-f]{12}$";
    public static final String OLD_IDAM_ID_PATTERN = "^[0-9]+$";
    public static final String IDAM_ID_PATTERN = "(" + UUID_PATTERN + ")|(" +  OLD_IDAM_ID_PATTERN + ")";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";
    public static final String TIMEZONE = "UTC";
    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    public static final String SERVICE_AUTHORIZATION2 = "ServiceAuthorization";

}
