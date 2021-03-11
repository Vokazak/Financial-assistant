package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.UUID;

public class TransToCategoryDaoTest {

    TransToCategoryDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoConfiguration.getTransactionToCategoryDao();
    }

    @Test
    public void insert_successful() throws SQLException {
        subj.insert(DaoConfiguration.getDataSource().getConnection(), 1, 1);
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() throws SQLException {
        subj.insert(DaoConfiguration.getDataSource().getConnection(), 2, 2);
    }

    @Test
    public void delete() throws SQLException {
        subj.delete(DaoConfiguration.getDataSource().getConnection(), 1);
    }

}
