package uk.gov.hmcts.reform.judicialbooking.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, UUID> {

    List<BookingEntity> findByUserIdInAndEndTimeGreaterThan(List<String> userIds, ZonedDateTime endTime);

    void deleteByUserId(String userId);
}