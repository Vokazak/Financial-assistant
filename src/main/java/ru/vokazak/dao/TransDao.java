package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import java.math.BigDecimal;
import java.sql.*;

@Service
public class TransDao {

    public TransDao() {
    }

    public TransModel insertFrom(Connection connection, String description, long accFromId, BigDecimal money) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_from, trans_money) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            Date transDate = new Date(System.currentTimeMillis());
            ps.setDate(1, new Date(System.currentTimeMillis()));
            ps.setString(2, description);
            ps.setLong(3, accFromId);
            ps.setBigDecimal(4, money);

            ps.executeUpdate();

            connection.commit();
            return getTransModel(ps.getGeneratedKeys(), transDate, description, money);
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public TransModel insertTo(Connection connection, String description, long accToId, BigDecimal money) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_to, trans_money) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            Date transDate = new Date(System.currentTimeMillis());
            ps.setDate(1, transDate);
            ps.setString(2, description);
            ps.setLong(3, accToId);
            ps.setBigDecimal(4, money);

            ps.executeUpdate();

            connection.commit();
            return getTransModel(ps.getGeneratedKeys(), transDate, description, money);
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }

    public TransModel insert(Connection connection, String description, long accFromId, long accToId, BigDecimal money) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into transaction (trans_date, description, acc_from, acc_to, trans_money) VALUES (?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            Date transDate = new Date(System.currentTimeMillis());
            ps.setDate(1, transDate);
            ps.setString(2, description);
            ps.setLong(3, accFromId);
            ps.setLong(4, accToId);
            ps.setBigDecimal(5, money);

            ps.executeUpdate();

            connection.commit();
            return getTransModel(ps.getGeneratedKeys(), transDate, description, money);
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    private TransModel getTransModel(ResultSet rs, Date date, String description, BigDecimal money) {
        try {
            if (rs.next()) {
                TransModel transModel = new TransModel();
                transModel.setId(rs.getLong(1));
                transModel.setDate(date);
                transModel.setDescription(description);
                transModel.setMoney(money);

                return transModel;
            } else {
                throw new UnsuccessfulCommandExecutionExc("Can't generate id");
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }
}
