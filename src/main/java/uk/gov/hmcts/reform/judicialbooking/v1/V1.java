package uk.gov.hmcts.reform.judicialbooking.v1;

public final class V1 {

    private V1() {
    }

    public static final class MediaType {

        private MediaType() {
        }

        // External API
        public static final String SERVICE = "application/vnd.uk.gov.hmcts.judicial-booking-service";
        public static final String CREATE_BOOKING = SERVICE
                + ".create-booking+json;charset=UTF-8;version=1.0";
        public static final String GET_BOOKINGS = SERVICE
                + ".get-bookings+json;charset=UTF-8;version=1.0";
    }

    public static final class Error {
        private Error() {
        }

        public static final String INVALID_REQUEST = "Request is not valid as per validation rule";
        public static final String BAD_REQUEST_INVALID_DATETIME = "Invalid Datetime Parameter";
        public static final String NO_RECORDS_FOUND_BY_ACTOR = "Bookings not found for Actor";
        public static final String UNPROCESSABLE_ENTITY_REQUEST_REJECTED =
                "Unprocessable entity as request has been rejected";

    }

}