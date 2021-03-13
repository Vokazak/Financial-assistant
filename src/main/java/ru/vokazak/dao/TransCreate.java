package ru.vokazak.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;
import ru.vokazak.service.AccountDTO;
import ru.vokazak.service.CategoryDTO;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TransCreate {

    private final DataSource dataSource;
    private final AccountDao accountDao;
    private final TransDao transDao;
    private final TransToCategoryDao transToCategoryDao;

    public TransCreate(DataSource dataSource, AccountDao accountDao, TransDao transDao, TransToCategoryDao transToCategoryDao) {
        this.dataSource = dataSource;
        this.accountDao = accountDao;
        this.transDao = transDao;
        this.transToCategoryDao = transToCategoryDao;
    }

    public TransModel createTransaction(String description, AccountDTO accFrom, AccountDTO accTo, CategoryDTO category, BigDecimal money) {

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setAutoCommit(false);

                //update accFrom
                if (accFrom != null) {
                    accountDao.update(connection, accFrom.getId(), accFrom.getBalance().subtract(money));
                }

                //update accTo
                if (accTo != null) {
                    accountDao.update(connection, accTo.getId(), accTo.getBalance().add(money));
                }

                //update transaction
                TransModel transModel;

                if (accFrom != null && accTo != null) {
                    transModel = transDao.insert(connection, description, accFrom.getId(), accTo.getId(), money);
                } else if (accTo != null) {
                    transModel = transDao.insertTo(connection, description, accTo.getId(), money);
                } else if (accFrom != null) {
                    transModel = transDao.insertFrom(connection, description, accFrom.getId(), money);
                } else throw new UnsuccessfulCommandExecutionExc("Invalid parameters");

                //update category
                transToCategoryDao.insert(connection, transModel.getId(), category.getId());

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
