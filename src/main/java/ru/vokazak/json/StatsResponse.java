package ru.vokazak.json;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class StatsResponse {
    Map<String, BigDecimal> stats;
}
