package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequestScope
public class PersistenceService {
    private BookingRepository bookingRepository;

    public PersistenceService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingEntity persistBooking(BookingEntity booking) {
        return bookingRepository.save(booking);
    }

    @Transactional
    public List<BookingEntity> getValidBookings(List<String> userIds) {
        return bookingRepository.findByUserIdInAndEndDateGreaterThan(userIds, LocalDate.now());
    }
}