package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vokazak.entity.Transaction;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransDaoTest {

    TransDao subj;
    ApplicationContext context;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.vokazak");
        subj = context.getBean(TransDao.class);
    }

    /*
    @Test
    public void insert_to() throws SQLException {
        Transaction transaction = subj.insertTo(context.getBean(DataSource.class).getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("123.4"), transaction.getMoney());
    }


    @Test
    public void insert_from() throws SQLException {
        Transaction transaction = subj.insertFrom(context.getBean(DataSource.class).getConnection(), "description", 1, new BigDecimal("123.4"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("123.4"), transaction.getMoney());
    }

    @Test
    public void insert() throws SQLException {
        Transaction transaction = subj.insert(context.getBean(DataSource.class).getConnection(), "description", 1, 2, new BigDecimal("123.4"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("123.4"), transaction.getMoney());

    }

     */

}
