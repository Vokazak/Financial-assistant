package ru.vokazak.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDaoTest {

    UserDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem");
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = DaoFactory.getUserDao();
    }

    @After
    public void after() {
        DaoFactory.resetDataSource();
        DaoFactory.resetUserDao();
    }

    @Test
    public void findByEmailAndHash_successful() {
        UserModel userModel = subj.findByEmailAndHash("invdov@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

        assertEquals(1L, userModel.getId());
        assertEquals("invdov@gmail.com", userModel.getEmail());
        assertEquals("65fcbb39532c342a6a3c92fa9acf1157", userModel.getPassword());
    }

    @Test
    public void insert_successful() {
        UserModel userModel = subj.insert("Inna", "Vdovina", "invdov2@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

        assertEquals(3, userModel.getId());
        assertEquals("invdov2@gmail.com", userModel.getEmail());
        assertEquals("65fcbb39532c342a6a3c92fa9acf1157", userModel.getPassword());
        assertEquals("Inna", userModel.getName());
        assertEquals("Vdovina", userModel.getSurname());

    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        UserModel userModel = subj.insert("Inna", "Vdovina", "invdov@gmail.com", "65fcbb39532c342a6a3c92fa9acf1157");

    }
}