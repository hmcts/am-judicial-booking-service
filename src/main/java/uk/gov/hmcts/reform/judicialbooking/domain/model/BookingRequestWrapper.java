package uk.gov.hmcts.reform.judicialbooking.domain.model;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonRootName("bookingRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestWrapper {
    BookingRequest bookingRequest;
}
