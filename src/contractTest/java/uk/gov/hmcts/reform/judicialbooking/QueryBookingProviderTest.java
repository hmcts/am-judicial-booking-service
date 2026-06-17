package uk.gov.hmcts.reform.judicialbooking;

import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import org.mockito.Mockito;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.mockito.ArgumentMatchers.any;

@Provider("am_judicialBooking_query")
public class QueryBookingProviderTest extends BaseProviderTest {

    @State({"A query request is received with a valid userId passed"})
    public void querySingleValidUserId() {
        initQueryMocks();
    }

    private void initQueryMocks() {

        Mockito.when(persistenceService.getValidBookings(TestDataBuilder.buildRequestIds().getUserIds()))
                .thenReturn(TestDataBuilder.buildListOfBookings());

        Mockito.when(securityUtils.getUserId()).thenReturn("5629957f-4dcd-40b8-a0b2-e64ff5898b28");
        Mockito.when(correlationInterceptorUtil.preHandle(any()))
                .thenReturn("14a21569-eb80-4681-b62c-6ae2ed069e2d");

    }

}
