package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;

@Service
public class PersistenceService {
    private BookingRepository bookingRepository;

    public PersistenceService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingEntity persistBooking(BookingEntity booking) {

        return bookingRepository.save(booking);

    }
}
