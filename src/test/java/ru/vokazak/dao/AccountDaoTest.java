package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vokazak.entity.Account;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AccountDaoTest {

    AccountDao subj;
    ApplicationContext context;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.vokazak");
        subj = context.getBean(AccountDao.class);
    }

    @Test
    public void insert_successful() {
        Account account = subj.insert("TestAcc2", new BigDecimal("123.4"), 1);

        assertEquals(Long.valueOf(3), account.getId());
        assertEquals(new BigDecimal("123.4"), account.getBalance());
        assertEquals("TestAcc2", account.getName());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        Account account = subj.insert("TestAcc", new BigDecimal("123.4"), 4);
    }

    @Test
    public void update_successful() throws SQLException {
        subj.update(1, new BigDecimal("432.1"));
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void update_unsuccessful() throws SQLException {
        subj.update(4, new BigDecimal("-432.1"));
    }

    @Test
    public void delete_successful() {
        Account account = subj.insert("TestAcc2", new BigDecimal("123.4"), 1);

        Account accountDel = subj.delete("TestAcc2", 1);

        assertEquals(Long.valueOf(3), account.getId());
        assertEquals("TestAcc2", account.getName());
        assertEquals(new BigDecimal("123.4"), account.getBalance());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void delete_unsuccessful() {
        Account account = subj.delete("TestAcc", 2);
    }

    @Test
    public void findByUserId_successful() {
        List<Account> modelList = subj.findByUserId(1);

        assertEquals(2, modelList.size());
        Account accountModel = modelList.get(0);

        assertEquals(Long.valueOf(1), accountModel.getId());
        assertEquals("TestAcc", accountModel.getName());
        assertEquals(new BigDecimal("123.4"), accountModel.getBalance());
        assertEquals(Long.valueOf(1L), accountModel.getId());

        Account account2 = modelList.get(1);

        assertEquals(Long.valueOf(2), account2.getId());
        assertEquals("AnotherTestAcc", account2.getName());
        assertEquals(new BigDecimal("123.4"), account2.getBalance());
    }

    @Test
    public void findByUserId_unsuccessful() {
        List<Account> modelList = subj.findByUserId(2);

        assertEquals(0, modelList.size());

    }



}
