package uk.gov.hmcts.reform.orgrolemapping;


import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.judicialbooking.controller.BaseTest;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


@ActiveProfiles("ctest")

public abstract class BaseTestContract extends BaseTest {

}
