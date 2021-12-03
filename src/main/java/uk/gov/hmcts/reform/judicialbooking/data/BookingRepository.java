package uk.gov.hmcts.reform.judicialbooking.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, UUID> {

    List<BookingEntity> findByUserIdAndEndTimeGreaterThan(String userId, ZonedDateTime endTime);

    List<BookingEntity> findByUserIdInAndEndTimeGreaterThan(List<String> userIds, ZonedDateTime endTime);

    List<BookingEntity> findByUserIdAndStatusAndAppointmentId(String userId, String status, String appointmentId);
}