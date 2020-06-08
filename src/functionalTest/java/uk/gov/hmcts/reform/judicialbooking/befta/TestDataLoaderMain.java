package uk.gov.hmcts.reform.judicialbooking.befta;

public class TestDataLoaderMain {

    private TestDataLoaderMain() {
    }

    public static void main(String[] args) {
        new JudicialBookingAmTestAutomationAdapter().doLoadTestData();
    }

}
