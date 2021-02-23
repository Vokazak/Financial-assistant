package ru.vokazak.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    private final DataSource dataSource;

    public AccountDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);

        dataSource = new HikariDataSource(config);
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

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }


    public AccountModel delete(String name, long userId) {
        try (Connection connection = dataSource.getConnection()){

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
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account", e);
        }
    }

    private ResultSet findByNameAndUserId(Connection connection, String name, long userId) {
        try {
            PreparedStatement ps =  connection.prepareStatement(
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
        try (Connection connection = dataSource.getConnection()){

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