package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransDaoTest {

    TransDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoConfiguration.getTransDao();
    }

    @Test
    public void insert_to() throws SQLException {
        TransModel transModel = subj.insertTo(DaoConfiguration.getDataSource().getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(2, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());
    }


    @Test
    public void insert_from() throws SQLException {
        TransModel transModel = subj.insertFrom(DaoConfiguration.getDataSource().getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(2, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());
    }

    @Test
    public void insert() throws SQLException {
        TransModel transModel = subj.insert(DaoConfiguration.getDataSource().getConnection(), "description", 1, 2, new BigDecimal("123.4"));

        assertEquals(2, transModel.getId());
        assertEquals("description", transModel.getDescription());
        assertEquals(new BigDecimal("123.4"), transModel.getMoney());

    }

}
