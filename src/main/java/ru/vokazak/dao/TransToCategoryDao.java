package ru.vokazak.dao;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.sql.*;

public class TransToCategoryDao {

    private final DataSource dataSource;

    public TransToCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Connection connection, long transactionId, long categoryId) {

        /*
        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into transaction_to_category (transaction_id, category_id) VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setLong(1, transactionId);
                ps.setLong(2, categoryId);
                ps.executeUpdate();

            } catch (SQLException e) {
                connection.rollback();
                throw new UnsuccessfulCommandExecutionExc(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

         */

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction_to_category (transaction_id, category_id) VALUES (?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, transactionId);
            ps.setLong(2, categoryId);
            ps.executeUpdate();

        } catch (SQLException e) {

            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

}
