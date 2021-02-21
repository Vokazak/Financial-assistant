package ru.vokazak.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    private final DataSource dataSource;

    public UserDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);

        dataSource = new HikariDataSource(config);
    }

    public UserModel findByEmailAndHash(String email, String hash) {

        UserModel userModel = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "select * from sys_user where email = ? and password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, hash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userModel = new UserModel();
                userModel.setId(rs.getLong("id"));
                userModel.setEmail(rs.getString("email"));
                userModel.setPassword(rs.getString("password"));
                userModel.setName(rs.getString("name"));
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

        return userModel;
    }

    public UserModel insert(String name, String surname, String email, String hash) {

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement ps = connection.prepareStatement(
                    "insert into sys_user (name, surname, password, email) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, hash);
            ps.setString(4, email);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                UserModel userModel = new UserModel();
                userModel.setId(rs.getLong(1));
                userModel.setEmail(email);
                userModel.setPassword(hash);
                userModel.setName(name);
                userModel.setSurname(surname);

                return userModel;
            } else {
                throw new UnsuccessfulCommandExecutionExc("Can't generate id");
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

    }

}
