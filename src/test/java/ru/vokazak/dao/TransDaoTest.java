package ru.vokazak.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class TransDaoTest {

    TransDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem");
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoFactory.getTransDao();
    }

    @After
    public void after() {
        DaoFactory.resetDataSource();
        DaoFactory.resetTransDao();
    }

    @Test
    public void insert_to() throws SQLException {
        TransModel transModel = subj.insertTo(DaoFactory.getDataSource().getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(3, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_to_unsuccessful() throws SQLException {
        TransModel transModel = subj.insertTo(DaoFactory.getDataSource().getConnection(), "description", 4, new BigDecimal("123.4"));
    }

    @Test
    public void insert_from() throws SQLException {
        TransModel transModel = subj.insertFrom(DaoFactory.getDataSource().getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(4, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());
    }

    @Test
    public void insert() throws SQLException {
        TransModel transModel = subj.insert(DaoFactory.getDataSource().getConnection(), "description", 1, 2, new BigDecimal("123.4"));

        assertEquals(2, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());

    }

}
