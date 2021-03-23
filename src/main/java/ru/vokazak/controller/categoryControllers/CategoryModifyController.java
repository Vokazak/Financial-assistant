package ru.vokazak.controller.categoryControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.controller.SecureController;
import ru.vokazak.json.CategoryRequest;
import ru.vokazak.json.CategoryResponse;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.CategoryService;

import java.util.Collections;

@Service("/category/modify")
@RequiredArgsConstructor
public class CategoryModifyController implements SecureController<CategoryRequest, CategoryResponse> {

    private final CategoryService categoryService;

    @Override
    public CategoryResponse handle(CategoryRequest request, Long userId) {

        CategoryDTO categoryDTO = categoryService.modify(request.getName(), request.getNewName());

        return new CategoryResponse(Collections.singletonList(categoryDTO));
    }

    @Override
    public Class<CategoryRequest> getRequestClass() {
        return CategoryRequest.class;
    }
}

