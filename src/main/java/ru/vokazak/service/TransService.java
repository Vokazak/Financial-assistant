package ru.vokazak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.TransCreate;
import ru.vokazak.dao.TransModel;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransService {

    private final Converter<TransModel, TransDTO> converter;
    private final TransCreate transCreate;

    public TransDTO createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        TransModel transModel = transCreate.createTransaction(description, accFrom, accTo, category, money);
        return converter.convert(transModel);
    }

}

