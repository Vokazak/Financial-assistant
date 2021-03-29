package ru.vokazak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.CategoryDao;
import ru.vokazak.entity.Category;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryDao categoryDao;
    private final Converter<Category, CategoryDTO> converter;

    public CategoryDTO create(String name) {

        Category category = categoryDao.insert(name);
        if (category == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while creating category");
        }

        return converter.convert(category);
    }

    public CategoryDTO delete(String name) {

        Category category = categoryDao.delete(name);
        if (category == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return converter.convert(category);
    }

    public CategoryDTO modify(String oldName, String newName) {

        Category category = categoryDao.update(oldName, newName);
        if (category == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return converter.convert(category);
    }

    public Map<CategoryDTO, BigDecimal> getMoneySpentForEachTransType(long userId, int days) {

        Map<Category, BigDecimal> categoryStats = categoryDao.sumMoneyForEachCategory(userId, days);
        if (categoryStats == null) {
            throw new UnsuccessfulCommandExecutionExc("Error in CategoryService while deleting category");
        }

        return categoryStats
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> converter.convert(e.getKey()), Map.Entry::getValue));
    }

    public List<CategoryDTO> getAll() {
        List<Category> categoryModelList = categoryDao.selectAll();
        if (categoryModelList.isEmpty()) {
            throw new UnsuccessfulCommandExecutionExc("No categories found in data base");
        }

        return categoryModelList
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());

    }
}
