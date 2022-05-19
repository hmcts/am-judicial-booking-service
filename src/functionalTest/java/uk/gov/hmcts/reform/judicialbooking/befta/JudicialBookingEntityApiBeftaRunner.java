package uk.gov.hmcts.reform.judicialbooking.befta;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import uk.gov.hmcts.befta.BeftaMain;


@RunWith(Cucumber.class)
@CucumberOptions(plugin = "json:target/cucumber.json",
                 glue = "uk.gov.hmcts.befta.player",
                 features = {"classpath:features"},
strict = true)
public class JudicialBookingEntityApiBeftaRunner {

    private JudicialBookingEntityApiBeftaRunner() {
    }

    @BeforeClass
    public static void setUp() {
        BeftaMain.setUp(new JudicialBookingAmTestAutomationAdapter());
    }

    @AfterClass
    public static void tearDown() {
        BeftaMain.tearDown();
    }

}
