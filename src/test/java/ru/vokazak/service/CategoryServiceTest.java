package ru.vokazak.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.vokazak.converter.CategoryModelToCategoryDTOConverter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.dao.CategoryModel;
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
    @Mock CategoryModelToCategoryDTOConverter converter;

    @Test
    public void create_successful() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("Salary");

        when(categoryDao.insert("Salary")).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Salary");

        when(converter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = subj.create("Salary");
        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).insert("Salary");
        verify(converter, times(1)).convert(categoryModel);
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
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("Salary");

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
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName("Salary");

        when(categoryDao.modify("Salary", "Income")).thenReturn(categoryModel);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Salary");

        when(converter.convert(categoryModel)).thenReturn(categoryDTO);

        CategoryDTO category = subj.modify("Salary", "Income");
        assertNotNull(category);
        assertEquals(categoryDTO, category);

        verify(categoryDao, times(1)).modify("Salary", "Income");
        verify(converter, times(1)).convert(categoryModel);
    }

    @Test
    public void modify_unsuccessful() {
        when(categoryDao.modify("Salary" ,"Income")).thenReturn(null);

        assertThrows(UnsuccessfulCommandExecutionExc.class, () ->
                subj.modify("Salary", "Income")
        );

        verify(categoryDao, times(1)).modify("Salary", "Income");
        verifyZeroInteractions(converter);
    }

    @Test
    public void getMoneySpent_successful() {
        CategoryModel categoryModel1 = new CategoryModel();
        categoryModel1.setId(1);
        categoryModel1.setName("Salary");

        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.setId(2);
        categoryModel2.setName("Purchase");

        Map<CategoryModel, BigDecimal> categoryStats = new HashMap<>();
        categoryStats.put(categoryModel1, new BigDecimal("123.4"));
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

        when(converter.convert(categoryModel1)).thenReturn(categoryDTO1);
        when(converter.convert(categoryModel2)).thenReturn(categoryDTO2);

        Map<CategoryDTO, BigDecimal> result = subj.getMoneySpentForEachTransType(1, 60);
        assertNotNull(result);
        assertEquals(result, accountDTOMap);

        verify(categoryDao, times(1)).sumMoneyForEachCategory(1, 60);
        verify(converter, times(1)).convert(categoryModel1);
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
