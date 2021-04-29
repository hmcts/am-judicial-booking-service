package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.data.BookingRepository;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Booking;
import uk.gov.hmcts.reform.judicialbooking.util.PersistenceUtil;

@Service
public class PersistenceService {
    private BookingRepository bookingRepository;
    private PersistenceUtil persistenceUtil;

    public PersistenceService(BookingRepository bookingRepository, PersistenceUtil persistenceUtil) {
        this.bookingRepository = bookingRepository;
        this.persistenceUtil = persistenceUtil;
    }

    @Transactional
    public BookingEntity persistBooking(Booking booking) {

        BookingEntity bookingEntity = persistenceUtil.convertBookingToEntity(booking);

        return bookingRepository.save(bookingEntity);

    }

    @Transactional
    public BookingEntity updateBooking(BookingEntity bookingEntity) {

        return bookingRepository.save(bookingEntity);

    }
}
