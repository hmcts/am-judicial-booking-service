package uk.gov.hmcts.reform.judicialbooking.befta;

import uk.gov.hmcts.befta.BeftaMain;

public class JudicialBookingAmBeftaMain {

    private JudicialBookingAmBeftaMain() {
    }

    public static void main(String[] args) {

        BeftaMain.main(args, new JudicialBookingAmTestAutomationAdapter());
    }
}
