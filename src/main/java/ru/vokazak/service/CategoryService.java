package ru.vokazak.service;

import ru.vokazak.converter.CategoryModelToCategoryDTOConverter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.dao.CategoryModel;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

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
}
