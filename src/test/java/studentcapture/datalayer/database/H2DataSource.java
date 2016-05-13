package studentcapture.datalayer.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import studentcapture.config.StudentCaptureApplication;


/**
 * Created by c12ton on 5/10/16.
 */
@Configuration
public class H2DataSource {

    public static DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.h2.Driver");
        String confUrl = "jdbc:h2:mem:testdb;DATABASE_TO_UPPER=FALSE;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;";
        String pathTables = "'"+StudentCaptureApplication.ROOT+"/database/test/create-tables.sql"+"'";
        String initUrl = "INIT=runscript from  "+ pathTables +";";
        driverManagerDataSource.setUrl(confUrl+initUrl);
        return driverManagerDataSource;
    }
}