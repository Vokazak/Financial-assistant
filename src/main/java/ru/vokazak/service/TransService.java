package ru.vokazak.service;

import ru.vokazak.converter.Converter;
import ru.vokazak.dao.*;
import ru.vokazak.atomicTransaction.TransRep;

import java.math.BigDecimal;

public class TransService {

    private final Converter<TransModel, TransDTO> converter;
    private final TransRep transRep;

    public TransService(TransRep transRep, Converter<TransModel, TransDTO> converter) {
        this.transRep =  transRep;
        this.converter = converter;
    }

    public TransDTO createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        TransModel transModel = transRep.createTransaction(description, accFrom, accTo, category, money);
        return converter.convert(transModel);
    }

}

