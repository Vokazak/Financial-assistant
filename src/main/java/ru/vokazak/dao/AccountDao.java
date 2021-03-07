package ru.vokazak.dao;

import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private final DataSource dataSource;

    public AccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AccountModel insert(String name, BigDecimal balance, long userId) {
        try (Connection connection = dataSource.getConnection()) {

            if (findByNameAndUserId(connection, name, userId).next()) {
                throw new UnsuccessfulCommandExecutionExc("Account with this name already exists");
            }

            PreparedStatement ps = connection.prepareStatement(
                    "insert into account (name, user_id, balance) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            ps.setLong(2, userId);
            ps.setBigDecimal(3, balance);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                AccountModel acc = new AccountModel();
                acc.setId(rs.getLong(1));
                acc.setName(name);
                acc.setUserId(userId);
                acc.setBalance(balance);

                return acc;
            } else {
                throw new UnsuccessfulCommandExecutionExc("Can't generate id");
            }

        } catch (SQLException | UnsuccessfulCommandExecutionExc e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public void update(Connection connection, long id, BigDecimal balance) {

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new UnsuccessfulCommandExecutionExc("Insufficient funds");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "update account set balance = ? where id = ?;"
            );
            ps.setBigDecimal(1, balance);
            ps.setLong(2, id);
            ps.execute();

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }

    public AccountModel delete(String name, long userId) {
        try (Connection connection = dataSource.getConnection()) {

            ResultSet rs = findByNameAndUserId(connection, name, userId);
            if (!rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Account with this name does not exist");
            }
            BigDecimal balance = rs.getBigDecimal("balance");
            long id = rs.getLong("id");

            PreparedStatement ps = connection.prepareStatement(
                    "delete from account as a where a.user_id = ? and a.name = ?;"
            );
            ps.setLong(1, userId);
            ps.setString(2, name);

            ps.execute();

            AccountModel acc = new AccountModel();
            acc.setId(id);
            acc.setName(name);
            acc.setUserId(userId);
            acc.setBalance(balance);

            return acc;

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account, since you have transactions from/to \"" + name + "\" account", e);
        }
    }

    private ResultSet findByNameAndUserId(Connection connection, String name, long userId) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "select * from account as a where a.name = ? and a.user_id = ?;"
            );
            ps.setString(1, name);
            ps.setLong(2, userId);

            return ps.executeQuery();

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public List<AccountModel> findByUserId(long userId) {
        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(
                    "select * from account as a where a.user_id = ? ;",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            List<AccountModel> accModelList = new ArrayList<>();

            while (rs.next()) {
                AccountModel acc = new AccountModel();
                acc.setUserId(userId);
                acc.setName(rs.getString("name"));
                acc.setBalance(rs.getBigDecimal("balance"));
                acc.setId(rs.getLong("id"));

                accModelList.add(acc);
            }

            return accModelList;

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while listing accs", e);
        }
    }
}
