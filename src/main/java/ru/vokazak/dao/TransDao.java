package ru.vokazak.dao;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

public class TransDao {

    private final DataSource dataSource;

    public TransDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransModel insertFrom(Connection connection, String description, long accFromId, BigDecimal money) {
        /*
        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into transaction (trans_date, description, acc_from, trans_money) VALUES (?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setDate(1, new Date(System.currentTimeMillis()));
                ps.setString(2, description);
                ps.setLong(3, accFromId);
                ps.setBigDecimal(4, money);

                ps.executeUpdate();

                connection.commit();
                return getTransModel(ps.getGeneratedKeys());
            } catch (SQLException e) {
                connection.rollback();
                throw new UnsuccessfulCommandExecutionExc(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while creating transaction", e);
        }
         */

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_from, trans_money) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, description);
            ps.setLong(3, accFromId);
            ps.setBigDecimal(4, money);

            ps.executeUpdate();

            return getTransModel(ps.getGeneratedKeys());
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public TransModel insertTo(Connection connection, String description, long accToId, BigDecimal money) {
        /*
        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into transaction (trans_date, description, acc_to, trans_money) VALUES (?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setDate(1, new Date(System.currentTimeMillis()));
                ps.setString(2, description);
                ps.setLong(3, accToId);
                ps.setBigDecimal(4, money);

                ps.executeUpdate();

                connection.commit();
                return getTransModel(ps.getGeneratedKeys());
            } catch (SQLException e) {
                connection.rollback();
                throw new UnsuccessfulCommandExecutionExc(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while creating transaction", e);
        }

         */

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_to, trans_money) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, description);
            ps.setLong(3, accToId);
            ps.setBigDecimal(4, money);

            ps.executeUpdate();

            connection.commit();
            return getTransModel(ps.getGeneratedKeys());
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }

    public TransModel insert(Connection connection, String description, long accFromId, long accToId, BigDecimal money) {
        /*
        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into transaction (trans_date, description, acc_from, acc_to, trans_money) VALUES (?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setDate(1, new Date(System.currentTimeMillis()));
                ps.setString(2, description);
                ps.setLong(3, accFromId);
                ps.setLong(4, accToId);
                ps.setBigDecimal(5, money);

                ps.executeUpdate();

                connection.commit();
                return getTransModel(ps.getGeneratedKeys());
            } catch (SQLException e) {
                connection.rollback();
                throw new UnsuccessfulCommandExecutionExc(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while creating transaction", e);
        }
         */

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_from, acc_to, trans_money) VALUES (?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, description);
            ps.setLong(3, accFromId);
            ps.setLong(4, accToId);
            ps.setBigDecimal(5, money);

            ps.executeUpdate();

            return getTransModel(ps.getGeneratedKeys());
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public TransModel getTransModel(ResultSet rs) {
        try {
            if (rs.next()) {
                TransModel transModel = new TransModel();
                transModel.setId(rs.getLong(1));
                transModel.setDate(rs.getDate("trans_date"));
                transModel.setDescription(rs.getString("description"));
                transModel.setMoney(rs.getBigDecimal("trans_money"));

                return transModel;
            } else {
                throw new UnsuccessfulCommandExecutionExc("Can't generate id");
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }
}
