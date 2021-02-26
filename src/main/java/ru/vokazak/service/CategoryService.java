package ru.vokazak.service;

import ru.vokazak.converter.CategoryModelToCategoryDTOConverter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.dao.CategoryModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CategoryService {

    private final CategoryDao categoryDao;
    private final CategoryModelToCategoryDTOConverter converter;

    public CategoryService() {
        this.categoryDao = new CategoryDao();
        this.converter = new CategoryModelToCategoryDTOConverter();
    }

    public CategoryDTO create(String name) {

        CategoryModel categoryModel = categoryDao.insert(name);
        if (categoryModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while creating category");
        }

        return converter.convert(categoryModel);
    }

    public CategoryDTO delete(String name) {

        CategoryModel categoryModel = categoryDao.delete(name);
        if (categoryModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return converter.convert(categoryModel);
    }

    public CategoryDTO modify(String oldName, String newName) {

        CategoryModel categoryModel = categoryDao.modify(oldName, newName);
        if (categoryModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return converter.convert(categoryModel);
    }

    public HashMap<CategoryDTO, BigDecimal> getMoneySpentForEachTransType(long userId, int days) {

        HashMap<CategoryModel, BigDecimal> categoryStats = categoryDao.sumMoneyForEachCategory(userId, days);
        if (categoryStats == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        HashMap<CategoryDTO, BigDecimal> resultMap = new HashMap<>();

        Set<Map.Entry<CategoryModel, BigDecimal>> entrySet = categoryStats.entrySet();
        entrySet.forEach(
                c -> {
                    CategoryDTO category = new CategoryDTO();
                    category.setId(c.getKey().getId());
                    category.setName(c.getKey().getName());

                    resultMap.put(category, c.getValue());
                }
        );

        return resultMap;
    }
}
