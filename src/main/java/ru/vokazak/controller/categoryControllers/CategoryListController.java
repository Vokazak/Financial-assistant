package ru.vokazak.controller.categoryControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.controller.SecureController;
import ru.vokazak.json.CategoryRequest;
import ru.vokazak.json.CategoryResponse;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.CategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("/category/list")
@RequiredArgsConstructor
public class CategoryListController implements SecureController<CategoryRequest, CategoryResponse> {

    private final CategoryService categoryService;

    @Override
    public CategoryResponse handle(CategoryRequest request, Long userId) {

        List<CategoryDTO> categories = categoryService.getAll();

        return new CategoryResponse(categories);
    }

    @Override
    public Class<CategoryRequest> getRequestClass() {
        return CategoryRequest.class;
    }
}

