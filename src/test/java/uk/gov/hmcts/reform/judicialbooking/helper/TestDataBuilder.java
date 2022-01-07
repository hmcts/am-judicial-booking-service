package uk.gov.hmcts.reform.judicialbooking.helper;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.reform.judicialbooking.data.BookingEntity;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingQueryResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingRequest;
import uk.gov.hmcts.reform.judicialbooking.domain.model.BookingResponse;
import uk.gov.hmcts.reform.judicialbooking.domain.model.UserRequest;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
public class TestDataBuilder {

    static String uuidString = "5629957f-4dcd-40b8-a0b2-e64ff5898b28";
    static String uuidString2 = "5629957f-4dcd-40b8-a0b2-e64ff5898b29";
    static UUID uuidString3 = UUID.fromString("5629957f-4dcd-40b8-a0b2-e64ff5898b30");

    private TestDataBuilder() {
        //not meant to be instantiated.
    }

    public static ResponseEntity<BookingResponse> buildCreateBookingResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(new BookingResponse());
    }

    public static BookingResponse buildBookingResponse(BookingEntity booking) {
        return new BookingResponse(booking);
    }

    public static BookingQueryResponse buildQueryResponse(List<BookingEntity> bookingEntities) {
        return new BookingQueryResponse(bookingEntities);
    }

    public static BookingRequest buildBookingRequest() {
        return BookingRequest.builder()
                .beginDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusMonths(1))
                .build();
    }

    public static BookingRequest buildWrongBooking() {
        return BookingRequest.builder()
                .beginDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now())
                .build();
    }

    public static BookingEntity buildParsedBooking() {
        return BookingEntity.builder()
                .userId(uuidString)
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildPreparedBooking() {
        return BookingEntity.builder()
                .id(uuidString3)
                .locationId("baseLocationId")
                .regionId("regionId")
                .userId(uuidString)
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static BookingEntity buildRetrievedBooking(String userId) {
        return BookingEntity.builder()
                .id(uuidString3)
                .locationId("LDN")
                .regionId("South-East")
                .userId(userId)
                .created(ZonedDateTime.now())
                .beginTime(ZonedDateTime.now().plusDays(1))
                .endTime(ZonedDateTime.now().plusMonths(1))
                .build();
    }

    public static UserRequest buildRequestIds() {
        return UserRequest.builder().userIds(List.of(uuidString, uuidString2)).build();
    }

    public static List<BookingEntity> buildListOfBookings() {
        return List.of(TestDataBuilder.buildRetrievedBooking(uuidString),
                TestDataBuilder.buildRetrievedBooking(uuidString2));
    }

    public static ResponseEntity<BookingQueryResponse> buildQueryResponseEntity() {
        return ResponseEntity.ok(TestDataBuilder.buildQueryResponse(buildListOfBookings()));
    }

    public static UserInfo buildUserInfo(String uuid) {
        List<String> list = new ArrayList<>();
        return UserInfo.builder().sub("sub").uid(uuid)
                .name("James").givenName("007").familyName("Bond").roles(list).build();
    }

}
