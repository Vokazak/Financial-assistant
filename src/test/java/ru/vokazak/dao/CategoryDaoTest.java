package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.vokazak.entity.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CategoryDaoTest {

    CategoryDao subj;

    @Before
    public void setUp() {
        System.setProperty("jdbcUrl", "jdbc:h2:mem:test_mem" + UUID.randomUUID().toString());
        System.setProperty("jdbcUser", "sa");
        System.setProperty("jdbcPassword", "34127856");
        System.setProperty("liquibaseFile", "liquibase_user_dao_test.xml");

        subj = new AnnotationConfigApplicationContext("ru.vokazak").getBean(CategoryDao.class);
    }

    @Test
    public void insert_successful() {
        Category category = subj.insert("Salary");

        assertEquals(Long.valueOf(2L), category.getId());
        assertEquals("Salary", category.getTransType());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        Category categoryModel = subj.insert("Purchase");
    }

    @Test
    public void delete_successful() {
        Category categoryModel = subj.insert("TestCategory");

        Category categoryModelDel = subj.delete("TestCategory");

        assertEquals(Long.valueOf(2L), categoryModel.getId());
        assertEquals("TestCategory", categoryModel.getTransType());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void delete_unsuccessful() {
        Category categoryModel = subj.delete("Salary");
    }

    @Test
    public void update_successful() {
        Category categoryModel = subj.update("Purchase", "Salary");

        assertEquals(Long.valueOf(1L), categoryModel.getId());
        assertEquals("Salary", categoryModel.getTransType());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void update_unsuccessful() {
        Category categoryModel = subj.update("Salary", "Income");
    }

    @Test
    public void selectAll() {
        List<Category> categoryList = subj.selectAll();

        assertEquals(1, categoryList.size());

        Category category = categoryList.get(0);
        assertEquals(Long.valueOf(1L), category.getId());
        assertEquals("Purchase", category.getTransType());

    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void sumMoneyForEachCategory_unsuccessful() {
        Map<Category, BigDecimal> stats = subj.sumMoneyForEachCategory(2, 20);
    }

}
