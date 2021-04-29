package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;
import uk.gov.hmcts.reform.judicialbooking.util.PersistenceUtil;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class PersistenceServiceTest {

    private final PersistenceUtil persistenceUtil = mock(PersistenceUtil.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);

    @InjectMocks
    private PersistenceService persistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void persistBooking() {
        Booking preparedBooking = TestDataBuilder.buildPreparedBooking();
        BookingEntity bookingEntity = TestDataBuilder.buildBookingEntity(preparedBooking);

        Mockito.when(persistenceUtil.convertBookingToEntity(preparedBooking)).thenReturn(bookingEntity);
        Mockito.when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);

        BookingEntity bookingEntityResponse = persistenceService.persistBooking(preparedBooking);

        assertNotNull(bookingEntityResponse);
    }

    @Test
    void updateBooking() {
        Booking preparedBooking = TestDataBuilder.buildPreparedBooking();
        BookingEntity bookingEntity = TestDataBuilder.buildBookingEntity(preparedBooking);

        Mockito.when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);

        BookingEntity bookingEntityResponse = persistenceService.updateBooking(bookingEntity);

        assertNotNull(bookingEntityResponse);
    }
}