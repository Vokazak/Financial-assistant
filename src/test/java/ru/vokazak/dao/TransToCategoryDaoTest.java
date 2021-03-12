package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

public class TransToCategoryDaoTest {

    TransToCategoryDao subj;
    ApplicationContext context;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.vokazak");
        subj = context.getBean(TransToCategoryDao.class);
    }

    @Test
    public void insert_successful() throws SQLException {
        subj.insert(context.getBean(DataSource.class).getConnection(), 1, 1);
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() throws SQLException {
        subj.insert(context.getBean(DataSource.class).getConnection(), 2, 2);
    }

    @Test
    public void delete() throws SQLException {
        subj.delete(context.getBean(DataSource.class).getConnection(), 1);
    }

}
