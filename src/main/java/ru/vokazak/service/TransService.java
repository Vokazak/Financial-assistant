package ru.vokazak.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vokazak.converter.Converter;
import ru.vokazak.dao.TransCreate;
import ru.vokazak.entity.Transaction;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransService {

    private final Converter<Transaction, TransDTO> converter;
    private final TransCreate transCreate;

    public TransDTO createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        Transaction transaction = transCreate.createTransaction(description, accFrom, accTo, category, money);
        return converter.convert(transaction);
    }

}

