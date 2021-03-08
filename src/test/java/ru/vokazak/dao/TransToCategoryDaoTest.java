package ru.vokazak.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class TransToCategoryDaoTest {

    TransToCategoryDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem");
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoFactory.getTransactionToCategoryDao();
    }

    @After
    public void after() {
        DaoFactory.resetDataSource();
        DaoFactory.resetTransToCategoryDao();
    }

    @Test
    public void insert_successful() throws SQLException {
        subj.insert(DaoFactory.getDataSource().getConnection(), 1, 1);
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() throws SQLException {
        subj.insert(DaoFactory.getDataSource().getConnection(), 2, 2);
    }

    @Test
    public void delete() throws SQLException {
        subj.delete(DaoFactory.getDataSource().getConnection(), 1);
    }

}
