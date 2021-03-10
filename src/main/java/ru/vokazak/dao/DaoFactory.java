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

import javax.sql.DataSource;
import java.sql.SQLException;

public class DaoFactory {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    private static UserDao userDao;
    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao(getDataSource());
        }

        return userDao;
    }
    public static void resetUserDao() {
        userDao = null;
        userDao = getUserDao();
    }

    private static CategoryDao categoryDao;
    public static CategoryDao getCategoryDao() {
        if (categoryDao == null) {
            categoryDao = new CategoryDao(getDataSource());
        }

        return categoryDao;
    }
    public static void resetCategoryDao() {
        categoryDao = null;
        categoryDao = getCategoryDao();
    }

    private static AccountDao accountDao;
    public static AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDao(getDataSource());
        }

        return accountDao;
    }
    public static void resetAccountDao() {
        accountDao = null;
        accountDao = getAccountDao();
    }

    private static TransDao transDao;
    public static TransDao getTransDao() {
        if (transDao == null) {
            transDao = new TransDao();
        }

        return transDao;
    }
    public static void resetTransDao() {
        transDao = null;
        transDao = getTransDao();
    }

    private static TransToCategoryDao transToCategoryDao;
    public static TransToCategoryDao getTransactionToCategoryDao() {
        if (transToCategoryDao == null) {
            transToCategoryDao = new TransToCategoryDao();
        }

        return transToCategoryDao;
    }
    public static void resetTransToCategoryDao() {
        transToCategoryDao = null;
        transToCategoryDao = getTransactionToCategoryDao();
    }

    private static DataSource dataSource;
    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getProperty("jdbcUrl", URL)); //URL - defaultParameter
            config.setUsername(System.getProperty("jdbcUser", USER));
            config.setPassword(System.getProperty("jdbcPassword", PASS));

            dataSource = new HikariDataSource(config);

            initDataBase();
        }

        return dataSource;
    }

    public static void resetDataSource() {
        dataSource = null;
        dataSource = getDataSource();
    }

    private static void initDataBase() {
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
