package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vokazak.entity.Account;
import ru.vokazak.entity.Category;
import ru.vokazak.entity.Transaction;
import ru.vokazak.service.AccountDTO;
import ru.vokazak.service.CategoryDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TransCreateTest {

    TransCreate subj;
    ApplicationContext context;
    AccountDTO account1 = new AccountDTO();
    AccountDTO account2 = new AccountDTO();
    CategoryDTO category = new CategoryDTO();

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        context = new AnnotationConfigApplicationContext("ru.vokazak");
        subj = context.getBean(TransCreate.class);

        account1.setBalance(new BigDecimal("123.4"));
        account1.setId(1);
        account1.setUserId(1L);
        account1.setName("TestAcc");

        account2.setBalance(new BigDecimal("123.4"));
        account2.setId(2L);
        account2.setUserId(1L);
        account2.setName("AnotherTestAcc");

        category.setName("Purchase");
        category.setId(1L);

    }

    @Test
    public void insert_to() {
        Transaction transaction = subj.createTransaction("description", null, account1, category, new BigDecimal("123.4"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("123.4"), transaction.getMoney());
    }


    @Test
    public void insert_from() {
        Transaction transaction = subj.createTransaction("description", account1, null, category,new BigDecimal("123.4"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("123.4"), transaction.getMoney());
    }

    @Test
    public void insert() {
        Transaction transaction = subj.createTransaction( "description", account1, account2, category, new BigDecimal("12"));

        assertEquals(Long.valueOf(2), transaction.getId());
        assertEquals("description", transaction.getDescription());
        assertEquals(new BigDecimal("12"), transaction.getMoney());

    }



}
