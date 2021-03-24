package ru.vokazak.json;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {
    private String name;
    private BigDecimal balance;
}
