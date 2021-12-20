package uk.gov.hmcts.reform.judicialbooking.befta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.befta.DefaultTestAutomationAdapter;
import uk.gov.hmcts.befta.player.BackEndFunctionalTestScenarioContext;

import java.time.LocalDate;
import java.util.UUID;

public class JudicialBookingAmTestAutomationAdapter extends DefaultTestAutomationAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JudicialBookingAmTestAutomationAdapter.class);

    public void doLoadTestData() {

    }

    @Override
    public Object calculateCustomValue(BackEndFunctionalTestScenarioContext scenarioContext, Object key) {
        //the docAMUrl is is referring the self link in PR
        switch (key.toString()) {
            case ("generateUUID"):
                return UUID.randomUUID();
            case ("tomorrow"):
                return LocalDate.now().plusDays(1);
            default:
                return super.calculateCustomValue(scenarioContext, key);
        }
    }
}
