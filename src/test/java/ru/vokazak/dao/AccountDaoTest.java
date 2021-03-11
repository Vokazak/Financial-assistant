package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AccountDaoTest {

    AccountDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoConfiguration.getAccountDao();
    }

    @Test
    public void insert_successful() {
        AccountModel accountModel = subj.insert("TestAcc2", new BigDecimal("123.4"), 1);

        assertEquals(3, accountModel.getId());
        assertEquals(new BigDecimal("123.4"), accountModel.getBalance());
        assertEquals("TestAcc2", accountModel.getName());
        assertEquals(1, accountModel.getUserId());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        AccountModel accountModel = subj.insert("TestAcc", new BigDecimal("123.4"), 1);
    }

    @Test
    public void update_successful() throws SQLException {
        subj.update(DaoConfiguration.getDataSource().getConnection(), 1, new BigDecimal("432.1"));
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void update_unsuccessful() throws SQLException {
        subj.update(DaoConfiguration.getDataSource().getConnection(), 1, new BigDecimal("-432.1"));
    }

    @Test
    public void delete_successful() {
        AccountModel accountModel = subj.insert("TestAcc2", new BigDecimal("123.4"), 1);

        AccountModel accountModelDel = subj.delete("TestAcc2", 1);

        assertEquals(3, accountModel.getId());
        assertEquals("TestAcc2", accountModel.getName());
        assertEquals(new BigDecimal("123.4"), accountModel.getBalance());
        assertEquals(1, accountModel.getUserId());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void delete_unsuccessful() {
        AccountModel accountModel = subj.delete("TestAcc", 2);
    }

    @Test
    public void findByUserId_successful() {
        List<AccountModel> modelList = subj.findByUserId(1);

        assertEquals(2, modelList.size());
        AccountModel accountModel = modelList.get(0);

        assertEquals(1, accountModel.getId());
        assertEquals("TestAcc", accountModel.getName());
        assertEquals(new BigDecimal("123.4"), accountModel.getBalance());
        assertEquals(1, accountModel.getUserId());

        AccountModel accountModel2 = modelList.get(1);

        assertEquals(2, accountModel2.getId());
        assertEquals("AnotherTestAcc", accountModel2.getName());
        assertEquals(new BigDecimal("123.4"), accountModel2.getBalance());
        assertEquals(1, accountModel2.getUserId());
    }

    @Test
    public void findByUserId_unsuccessful() {
        List<AccountModel> modelList = subj.findByUserId(2);

        assertEquals(0, modelList.size());

    }



}
