package uk.gov.hmcts.reform.judicialbooking.befta;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import uk.gov.hmcts.befta.BeftaMain;

import static io.cucumber.core.options.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SuiteDisplayName("Cucumber Test Suite")
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "uk.gov.hmcts.befta.player")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber.json")
public class JudicialBookingEntityApiBeftaRunner {

    private JudicialBookingEntityApiBeftaRunner() {
    }

    @BeforeAll
    public static void setUp() {
        BeftaMain.setUp(new JudicialBookingAmTestAutomationAdapter());
    }

    @AfterAll
    public static void tearDown() {
        BeftaMain.tearDown();
    }

}
