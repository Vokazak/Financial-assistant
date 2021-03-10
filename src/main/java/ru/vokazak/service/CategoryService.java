package ru.vokazak.service;

import ru.vokazak.converter.Converter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.dao.CategoryModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryService {

    private final CategoryDao categoryDao;
    private final Converter<CategoryModel, CategoryDTO> converter;

    public CategoryService(CategoryDao categoryDao, Converter<CategoryModel, CategoryDTO> converter) {
        this.categoryDao = categoryDao;
        this.converter = converter;
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

        CategoryModel categoryModel = categoryDao.update(oldName, newName);
        if (categoryModel == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return converter.convert(categoryModel);
    }

    public Map<CategoryDTO, BigDecimal> getMoneySpentForEachTransType(long userId, int days) {

        Map<CategoryModel, BigDecimal> categoryStats = categoryDao.sumMoneyForEachCategory(userId, days);
        if (categoryStats == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return categoryStats
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> converter.convert(e.getKey()), Map.Entry::getValue));
    }

    public List<CategoryDTO> getAll() {
        List<CategoryModel> categoryModelList = categoryDao.selectAll();
        if (categoryModelList.isEmpty()) {
            throw new UnsuccessfulCommandExecutionExc("No categories found in data base");
        }

        return categoryModelList
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());

    }
}
