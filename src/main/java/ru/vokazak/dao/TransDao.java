package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Account;
import ru.vokazak.entity.Category;
import ru.vokazak.entity.Transaction;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

@Service
public class TransDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Transaction insertFrom(String description, Account accFrom, BigDecimal money, List<Category> categories) {
        try {
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setMoney(money);
            transaction.setAccFrom(accFrom);
            transaction.setCategories(categories);

            em.persist(transaction);
            return transaction;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public Transaction insertTo(String description, Account accTo, BigDecimal money, List<Category> categories) {
        try {
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setMoney(money);
            transaction.setAccTo(accTo);
            transaction.setCategories(categories);

            em.persist(transaction);
            return transaction;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }

    @Transactional
    public Transaction insert(String description, Account accFrom, Account accTo, BigDecimal money, List<Category> categories) {
        try {
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setDate(new Date(System.currentTimeMillis()));
            transaction.setMoney(money);
            transaction.setAccFrom(accFrom);
            transaction.setAccTo(accTo);
            transaction.setCategories(categories);

            em.persist(transaction);
            return transaction;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

}
