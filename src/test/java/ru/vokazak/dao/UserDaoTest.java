package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vokazak.entity.User;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserDaoTest {

    UserDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = new AnnotationConfigApplicationContext("ru.vokazak").getBean(UserDao.class);
    }

    @Test
    public void findByEmailAndHash_successful() {
        User user = subj.findByEmailAndHash("invdov@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

        assertEquals(Long.valueOf(1), user.getId());
        assertEquals("invdov@gmail.com", user.getEmail());
        assertEquals("65fcbb39532c342a6a3c92fa9acf1157", user.getPassword());
    }

    @Test
    public void insert_successful() {
        User user = subj.insert("Inna", "Vdovina", "invdov2@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

        assertEquals(Long.valueOf(2), user.getId());
        assertEquals("invdov2@gmail.com", user.getEmail());
        assertEquals("65fcbb39532c342a6a3c92fa9acf1157", user.getPassword());
        assertEquals("Inna", user.getName());
        assertEquals("Vdovina", user.getSurname());

    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        User user = subj.insert("Inna", "Vdovina", "invdov@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

    }
}