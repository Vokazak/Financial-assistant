package ru.vokazak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.json.StatsRequest;
import ru.vokazak.json.StatsResponse;
import ru.vokazak.service.CategoryDTO;
import ru.vokazak.service.CategoryService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service("/stats")
@RequiredArgsConstructor
public class StatsController implements SecureController<StatsRequest, StatsResponse> {

    private final CategoryService categoryService;

    @Override
    public StatsResponse handle(StatsRequest request, Long userId) {

        Map<CategoryDTO, BigDecimal> categoryStats = categoryService.getMoneySpentForEachTransType(userId, request.getDays());

        return new StatsResponse(
                categoryStats
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().getName(),
                                Map.Entry::getValue)
                        )
        );

    }

    @Override
    public Class<StatsRequest> getRequestClass() {
        return StatsRequest.class;
    }
}
