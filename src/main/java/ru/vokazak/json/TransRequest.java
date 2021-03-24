package ru.vokazak.json;

import lombok.Data;

@Data
public class TransRequest {

    private String transactionName;
    private String accFromName;
    private String accToName;
    private String categoryName;
    private String moneyValue;
}
