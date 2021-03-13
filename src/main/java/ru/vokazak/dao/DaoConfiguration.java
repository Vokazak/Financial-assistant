package ru.vokazak.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DaoConfiguration {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getProperty("jdbcUrl", URL)); //URL - defaultParameter
        config.setUsername(System.getProperty("jdbcUser", USER));
        config.setPassword(System.getProperty("jdbcPassword", PASS));

        DataSource dataSource = new HikariDataSource(config);
        initDataBase(dataSource);
        return dataSource;
    }

    private void initDataBase(DataSource dataSource) {
        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);

            Liquibase liquibase = new Liquibase(
                    System.getProperty("liquibaseFile", "liquibase.xml"),
                    new ClassLoaderResourceAccessor(), //liquibase.xml from classloader
                    database
            );

            liquibase.update(new Contexts()); //to update DB

        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
