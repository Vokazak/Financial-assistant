package ru.vokazak.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.sql.*;

public class CategoryDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    private final DataSource dataSource;

    public CategoryDao() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASS);

        dataSource = new HikariDataSource(config);
    }

    public CategoryModel insert(String name) {
        try (Connection connection = dataSource.getConnection()) {

            if (findByName(connection, name).next()) {
                throw new UnsuccessfulCommandExecutionExc("Category with this name already exists");
            }

            PreparedStatement ps = connection.prepareStatement(
                    "insert into category (trans_type) VALUES (?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                CategoryModel category = new CategoryModel();
                category.setId(rs.getLong(1));
                category.setName(name);

                return category;
            } else {
                throw new UnsuccessfulCommandExecutionExc("Can't generate id");
            }

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public CategoryModel delete(String name) {
        try (Connection connection = dataSource.getConnection()){

            ResultSet rs = findByName(connection, name);
            if (!rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Category with this name does not exist");
            }
            long id = rs.getLong("id");

            PreparedStatement ps = connection.prepareStatement(
                    "delete from category as a where a.trans_type = ? ;"
            );
            ps.setString(1, name);

            ps.execute();

            CategoryModel category = new CategoryModel();
            category.setId(id);
            category.setName(name);

            return category;

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account", e);
        }
    }

    public CategoryModel modify(String oldName, String newName) {
        try (Connection connection = dataSource.getConnection()){
            ResultSet rs = findByName(connection, oldName);
            if (!rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Category with this name does not exist");
            }
            long id = rs.getLong("id");

            //UPDATE mytable SET a = 5, b = 3, c = 1 WHERE a > 0;
            PreparedStatement ps = connection.prepareStatement(
                    "update category set trans_type = ? where trans_type = ?;"
            );
            ps.setString(1, newName);
            ps.setString(2, oldName);

            ps.execute();

            CategoryModel category = new CategoryModel();
            category.setId(id);
            category.setName(newName);
            return category;

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account", e);
        }
    }

    private ResultSet findByName(Connection connection, String name) {
        try {
            PreparedStatement ps =  connection.prepareStatement(
                    "select * from category as a where a.trans_type = ?;"
            );
            ps.setString(1, name);

            return ps.executeQuery();

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

}
