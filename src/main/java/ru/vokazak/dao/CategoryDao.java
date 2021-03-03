package ru.vokazak.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CategoryDao {

    private final DataSource dataSource;

    public CategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
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

    public Map<CategoryModel, BigDecimal> sumMoneyForEachCategory(long userId, int days) {

        try (Connection connection = dataSource.getConnection()){

            PreparedStatement ps = connection.prepareStatement(
                    "select\n" +
                            "    c.id,\n" +
                            "    c.trans_type,\n" +
                            "    sum(trans_money)\n" +
                            "from sys_user as u\n" +
                            "    left join account as a on u.id = a.user_id\n" +
                            "    left join transaction as t on a.id = t.acc_from or a.id = t.acc_to\n" +
                            "    left join transaction_to_category ttc on t.id = ttc.transaction_id\n" +
                            "    left join category c on ttc.category_id = c.id\n" +
                            "where u.id = ?" +
                            "    and t.trans_date between (CURRENT_TIMESTAMP - make_interval(days := ?)) and CURRENT_TIMESTAMP\n" +
                            "group by c.trans_type, c.id;"
            );
            ps.setLong(1, userId);
            ps.setInt(2, days);

            ResultSet rs = ps.executeQuery();

            Map<CategoryModel, BigDecimal> resultMap = new HashMap<>();

            while (rs.next()) {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setId(rs.getLong("id"));
                categoryModel.setName(rs.getString("trans_type"));

                BigDecimal money = rs.getBigDecimal("sum");

                resultMap.put(categoryModel, money);
            }

            if (resultMap.isEmpty()) {
                throw new UnsuccessfulCommandExecutionExc("No transactions in Data Base for " + days + " days period");
            }

            return resultMap;

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account", e);
        }
    }

}
