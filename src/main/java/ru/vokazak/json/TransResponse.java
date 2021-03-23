package ru.vokazak.json;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class TransResponse {
    private long id;
    private Date date;
    private String description;
    private BigDecimal money;
}
