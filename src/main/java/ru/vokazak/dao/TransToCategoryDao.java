package ru.vokazak.dao;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.sql.*;

public class TransToCategoryDao {

    public void insert(Connection connection, long transactionId, long categoryId) {

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

    public void delete(Connection connection, long transactionId) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "delete from transaction_to_category as ttc where ttc.transaction_id = ?;"
            );
            ps.setLong(1, transactionId);
            ps.executeUpdate();

        } catch (SQLException e) {

            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

}
