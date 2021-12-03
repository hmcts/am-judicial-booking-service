package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.List;

class PersistenceServiceTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);

    @InjectMocks
    private PersistenceService persistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void persistBooking() {
        BookingEntity preparedBooking = TestDataBuilder.buildPreparedBooking();

        Mockito.when(bookingRepository.save(preparedBooking)).thenReturn(preparedBooking);

        BookingEntity bookingEntityResponse = persistenceService.persistBooking(preparedBooking);

        assertNotNull(bookingEntityResponse);
    }

    @Test
    void updateBooking() {
        BookingEntity preparedBooking = TestDataBuilder.buildPreparedBooking();

        Mockito.when(bookingRepository.save(preparedBooking)).thenReturn(preparedBooking);

        BookingEntity bookingEntityResponse = persistenceService.persistBooking(preparedBooking);

        assertNotNull(bookingEntityResponse);
    }

    @Test
    void getValidBookings() {
        List<String> userIds = TestDataBuilder.buildRequestIds().getUserIds();

        List<BookingEntity> retrievedBookings = TestDataBuilder.buildListOfBookings();

        Mockito.when(bookingRepository.findByUserIdInAndEndTimeGreaterThan(any(), any())).thenReturn(retrievedBookings);

        List<BookingEntity> dbResponse = persistenceService.getValidBookings(userIds);

        assertNotNull(dbResponse);
        assertEquals(retrievedBookings, dbResponse);
    }
}