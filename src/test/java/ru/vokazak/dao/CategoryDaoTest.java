package ru.vokazak.dao;

import org.junit.Before;
import org.junit.Test;

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

        subj = DaoConfiguration.getCategoryDao();
    }

    @Test
    public void insert_successful() {
        CategoryModel categoryModel = subj.insert("Salary");

        assertEquals(2, categoryModel.getId());
        assertEquals("Salary", categoryModel.getName());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void insert_unsuccessful() {
        CategoryModel categoryModel = subj.insert("Purchase");
    }

    @Test
    public void delete_successful() {
        CategoryModel categoryModel = subj.insert("TestCategory");

        CategoryModel categoryModelDel = subj.delete("TestCategory");

        assertEquals(2, categoryModel.getId());
        assertEquals("TestCategory", categoryModel.getName());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void delete_unsuccessful() {
        CategoryModel categoryModel = subj.delete("Salary");
    }

    @Test
    public void update_successful() {
        CategoryModel categoryModel = subj.update("Purchase", "Salary");

        assertEquals(1, categoryModel.getId());
        assertEquals("Salary", categoryModel.getName());
    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void update_unsuccessful() {
        CategoryModel categoryModel = subj.update("Salary", "Income");
    }

    @Test
    public void selectAll() {
        List<CategoryModel> categoryList = subj.selectAll();

        assertEquals(1, categoryList.size());

        CategoryModel category = categoryList.get(0);
        assertEquals(1, category.getId());
        assertEquals("Purchase", category.getName());

    }

    @Test(expected = ru.vokazak.exception.UnsuccessfulCommandExecutionExc.class)
    public void sumMoneyForEachCategory_unsuccessful() {
        Map<CategoryModel, BigDecimal> stats = subj.sumMoneyForEachCategory(2, 20);
    }

}
