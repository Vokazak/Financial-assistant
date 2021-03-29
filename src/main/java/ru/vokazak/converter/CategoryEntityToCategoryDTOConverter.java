package ru.vokazak.converter;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Category;
import ru.vokazak.service.CategoryDTO;

@Service
public class CategoryEntityToCategoryDTOConverter implements Converter<Category, CategoryDTO> {

    @Override
    public CategoryDTO convert(Category source) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(source.getId());
        categoryDTO.setName(source.getTransType());

        return categoryDTO;
    }
}
