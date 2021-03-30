package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Account;
import ru.vokazak.entity.Category;
import ru.vokazak.entity.Transaction;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.AccountDTO;
import ru.vokazak.service.CategoryDTO;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

@Service
public class TransCreate {

    private final DataSource dataSource;
    private final AccountDao accountDao;
    private final TransDao transDao;

    public TransCreate(DataSource dataSource, AccountDao accountDao, TransDao transDao) {
        this.dataSource = dataSource;
        this.accountDao = accountDao;
        this.transDao = transDao;
    }

    public Transaction createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setAutoCommit(false);

                Account accountFrom = new Account();
                Account accountTo = new Account();
                Category c = new Category();
                c.setId(category.getId());
                c.setTransType(category.getName());

                //update accFrom
                if (accFrom != null) {
                    accountFrom.setBalance(accFrom.getBalance());
                    accountFrom.setUserId(accFrom.getUserId());
                    accountFrom.setName(accFrom.getName());
                    accountFrom.setId(accFrom.getId());

                    if (accFrom.getBalance().compareTo(money) >= 0) {
                        accountDao.update(accFrom.getId(), accFrom.getBalance().subtract(money));
                    } else {
                        throw new UnsuccessfulCommandExecutionExc("Insufficient funds");
                    }
                }

                //update accTo
                if (accTo != null) {
                    accountTo.setBalance(accTo.getBalance());
                    accountTo.setUserId(accTo.getUserId());
                    accountTo.setName(accTo.getName());
                    accountTo.setId(accTo.getId());
                    accountDao.update(accTo.getId(), accTo.getBalance().add(money));
                }

                //update transaction
                Transaction transModel;

                if (accFrom != null && accTo != null) {
                    transModel = transDao.insert(description, accountFrom, accountTo, money, Collections.singletonList(c));
                } else if (accTo != null) {
                    transModel = transDao.insertTo(description, accountTo, money, Collections.singletonList(c));
                } else if (accFrom != null) {
                    transModel = transDao.insertFrom(description, accountFrom, money, Collections.singletonList(c));
                } else throw new UnsuccessfulCommandExecutionExc("Invalid parameters");

                connection.commit();
                return transModel;
            } catch (SQLException | UnsuccessfulCommandExecutionExc e) {
                connection.rollback();
                throw new UnsuccessfulCommandExecutionExc(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }
}
