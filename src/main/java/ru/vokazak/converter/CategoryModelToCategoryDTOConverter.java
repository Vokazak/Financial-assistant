package ru.vokazak.converter;

import ru.vokazak.dao.CategoryModel;
import ru.vokazak.service.CategoryDTO;

public class CategoryModelToCategoryDTOConverter implements Converter<CategoryModel, CategoryDTO> {

    @Override
    public CategoryDTO convert(CategoryModel source) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(source.getId());
        categoryDTO.setName(source.getName());

        return categoryDTO;
    }
}
