package ru.vokazak.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.vokazak.service.CategoryDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private List<CategoryDTO> categories;
}
