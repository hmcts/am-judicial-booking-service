package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBooking;
import uk.gov.hmcts.reform.judicialbooking.domain.model.OrmBookingAssignmentsRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.enums.Status;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.UUID;

class PrepareDataServiceTest {

    private final PrepareDataService sut = new PrepareDataService();

    @Test
    void prepareBooking() {

        BookingEntity booking = sut.prepareBooking(TestDataBuilder.buildBooking(),
                Collections.singletonList(TestDataBuilder.buildJudicialProfile()));

        assertNotNull(booking);
        assertEquals(Status.NEW.toString(), booking.getStatus());

    }

    @Test
    void prepareOrmBookingRequest() {
        JudicialUserProfile judicialUserProfile = TestDataBuilder.buildJudicialProfile();
        BookingEntity booking = TestDataBuilder.buildPreparedBooking();
        String uuid = "5629957f-4dcd-40b8-a0b2-e64ff5898b29";
        booking.setId(UUID.fromString(uuid));
        OrmBookingAssignmentsRequest ormBookingRequest
                = sut.prepareOrmBookingRequest(booking, judicialUserProfile);

        assertNotNull(ormBookingRequest);
        assertNotNull(ormBookingRequest.getBookings());
        assertNotNull(ormBookingRequest.getBookingRequest());
        assertNotNull(ormBookingRequest.getBookingRequest().getAuthorisationIds());
        assertEquals(booking.getUserId(), ormBookingRequest.getBookingRequest().getActorId());
        assertEquals(uuid, ormBookingRequest.getBookings().get(0).getBookingId());
    }

    @Test
    void prepareBookingResponse() {
        ResponseEntity<BookingResponse> bookingResponseEntity =
                sut.prepareBookingResponse(TestDataBuilder.buildPreparedBooking());

        assertNotNull(bookingResponseEntity);
    }

    @Test
    void convertBookingToOrmBooking() {
        String bookingId = "5629957f-4dcd-40b8-a0b2-e64ff5898b30";
        BookingEntity preparedBooking = TestDataBuilder.buildPreparedBooking();
        preparedBooking.setId(UUID.fromString(bookingId));

        OrmBooking ormBooking = sut.convertBookingToOrmBooking(preparedBooking);

        assertNotNull(ormBooking);
        assertNotNull(ormBooking.getBookingId());
        assertEquals(bookingId, ormBooking.getBookingId());

    }

}