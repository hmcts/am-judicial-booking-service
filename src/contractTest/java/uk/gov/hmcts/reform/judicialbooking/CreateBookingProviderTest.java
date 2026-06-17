package uk.gov.hmcts.reform.judicialbooking;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.mockito.ArgumentMatchers.any;

@Provider("am_judicialBooking_create")
public class CreateBookingProviderTest extends BaseProviderTest {

    @State({"A create request is received with valid begin and end dates"})
    public void createSingleBooking() {
        initCreateMocks();
    }

    private void initCreateMocks() {

        Mockito.when(persistenceService.persistBooking(any()))
                .thenReturn(TestDataBuilder.buildPreparedBooking());

        Mockito.when(securityUtils.getUserId()).thenReturn("3168da13-00b3-41e3-81fa-cbc71ac28a0f");
        Mockito.when(correlationInterceptorUtil.preHandle(any()))
                .thenReturn("14a21569-eb80-4681-b62c-6ae2ed069e2d");

    }

}
