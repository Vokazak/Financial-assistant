package ru.vokazak.service;

import ru.vokazak.converter.Converter;
import ru.vokazak.dao.TransCreate;
import ru.vokazak.dao.TransModel;

import java.math.BigDecimal;

public class TransService {

    private final Converter<TransModel, TransDTO> converter;
    private final TransCreate transCreate;

    public TransService(TransCreate transCreate, Converter<TransModel, TransDTO> converter) {
        this.transCreate = transCreate;
        this.converter = converter;
    }

    public TransDTO createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        TransModel transModel = transCreate.createTransaction(description, accFrom, accTo, category, money);
        return converter.convert(transModel);
    }

}

