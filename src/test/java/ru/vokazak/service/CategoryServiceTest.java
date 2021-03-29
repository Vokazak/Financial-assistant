package ru.vokazak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.vokazak.converter.CategoryEntityToCategoryDTOConverter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.entity.Category;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks CategoryService subj;

    @Mock CategoryDao categoryDao;
    @Mock
    CategoryEntityToCategoryDTOConverter converter;

    @Test
    public void create_successful() {
        Category category = new Category();
        category.setId(1L);
        category.setTransType("Salary");

        when(categoryDao.insert("Salary")).thenReturn(category);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Salary");

        when(converter.convert(category)).thenReturn(categoryDTO);

        CategoryDTO categoryDTO1 = subj.create("Salary");
        assertNotNull(category);
        assertEquals(categoryDTO1, categoryDTO);

        verify(categoryDao, times(1)).insert("Salary");
        verify(converter, times(1)).convert(category);
    }

    @Test
    public void create_unsuccessful() {

        when(categoryDao.insert("Salary")).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, () ->
            subj.create("Salary")
        );

        verify(categoryDao, times(1)).insert("Salary");
        verifyZeroInteractions(converter);
    }

    @Test
    public void delete_successful() {
        Category categoryModel = new Category();
        categoryModel.setId(1L);
        categoryModel.setTransType("Salary");

        when(categoryDao.delete("Salary")).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Salary");

        when(converter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = subj.delete("Salary");
        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).delete("Salary");
        verify(converter, times(1)).convert(categoryModel);
    }

    @Test
    public void delete_unsuccessful() {

        when(categoryDao.delete("Salary")).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, () ->
                subj.delete("Salary")
        );

        verify(categoryDao, times(1)).delete("Salary");
        verifyZeroInteractions(converter);
    }

    @Test
    public void modify_successful() {
        Category categoryModel = new Category();
        categoryModel.setId(1L);
        categoryModel.setTransType("Salary");

        when(categoryDao.update("Salary", "Income")).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Salary");

        when(converter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = subj.modify("Salary", "Income");
        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).update("Salary", "Income");
        verify(converter, times(1)).convert(categoryModel);
    }

    @Test
    public void modify_unsuccessful() {
        when(categoryDao.update("Salary" ,"Income")).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, () ->
                subj.modify("Salary", "Income")
        );

        verify(categoryDao, times(1)).update("Salary", "Income");
        verifyZeroInteractions(converter);
    }

    @Test
    public void getMoneySpent_successful() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setTransType("Salary");

        Category categoryModel2 = new Category();
        categoryModel2.setId(2L);
        categoryModel2.setTransType("Purchase");

        Map<Category, BigDecimal> categoryStats = new HashMap<>();
        categoryStats.put(category1, new BigDecimal("123.4"));
        categoryStats.put(categoryModel2, new BigDecimal("432.1"));

        when(categoryDao.sumMoneyForEachCategory(1, 60)).thenReturn(categoryStats);

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1);
        categoryDTO1.setName("Salary");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2);
        categoryDTO2.setName("Purchase");

        Map<CategoryDTO, BigDecimal> accountDTOMap = new HashMap<>();
        accountDTOMap.put(categoryDTO1, new BigDecimal("123.4"));
        accountDTOMap.put(categoryDTO2, new BigDecimal("432.1"));

        when(converter.convert(category1)).thenReturn(categoryDTO1);
        when(converter.convert(categoryModel2)).thenReturn(categoryDTO2);

        Map<CategoryDTO, BigDecimal> result = subj.getMoneySpentForEachTransType(1, 60);
        assertNotNull(result);
        assertEquals(result, accountDTOMap);

        verify(categoryDao, times(1)).sumMoneyForEachCategory(1, 60);
        verify(converter, times(1)).convert(category1);
        verify(converter, times(1)).convert(categoryModel2);

    }

    @Test
    public void getMoneySpent_unsuccessful() {
        when(categoryDao.sumMoneyForEachCategory(1, 60)).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, ()-> {
            subj.getMoneySpentForEachTransType(1, 60);
        });

        verify(categoryDao, times(1)).sumMoneyForEachCategory(1, 60);

        verifyZeroInteractions(converter);
    }
}
