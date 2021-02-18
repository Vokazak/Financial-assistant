package ru.vokazak;

import java.math.BigDecimal;
import java.sql.*;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class Executor {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "34127856";

    private final CurrentUser currentUser;
    private Connection connection;

    Executor() {
        currentUser = new CurrentUser();

        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login(String email, String password) throws UnsuccessfulCommandExecutionExc {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "select * from sys_user where email = ? and password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, md5Hex(password));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Hello, " + rs.getString("name") + "!");
                currentUser.setEmail(email);
                currentUser.setId(rs.getInt("id"));
            } else {
                throw new UnsuccessfulCommandExecutionExc("Access denied");
            }

            ps.close();
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public void register(String email, String password, String name, String surname) throws UnsuccessfulCommandExecutionExc {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from sys_user as u where u.email = '" + email + "';");

            if (rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Account with this email already exists");
            }
            statement.close();

            PreparedStatement ps = connection.prepareStatement(
                    "insert into sys_user (name, surname, password, email) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, md5Hex(password));
            ps.setString(4, email);

            ps.execute();
            ps.close();

            System.out.println("Successfully registered");
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public void createAcc(String name, BigDecimal balance) throws UnsuccessfulCommandExecutionExc {
        checkLogin();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select *\n" +
                    "from account as u\n" +
                    "where u.name = '" + name + "';");

            if (rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Account with this name already exists");
            }
            statement.close();

            PreparedStatement ps = connection.prepareStatement(
                    "insert into account (name, user_id, balance) VALUES (?, ?, ?);"
            );
            ps.setString(1, name);
            ps.setInt(2, currentUser.getId());
            ps.setBigDecimal(3, balance);

            ps.execute();
            ps.close();

            System.out.println("Successfully created new acc");
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    public void deleteAcc(String name) throws UnsuccessfulCommandExecutionExc {
        checkLogin();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select\n" +
                    "    a.name\n" +
                    "from account as a\n" +
                    "where a.name = '" + name + "';");

            if (!rs.next()) {
                throw new UnsuccessfulCommandExecutionExc("Account with this name does not exist");
            }
            statement.close();

            PreparedStatement ps = connection.prepareStatement(
                        "delete from account as a\n" +
                                "where a.user_id = ?\n" +
                                "  and a.name = ?;"
            );
            ps.setInt(1, currentUser.getId());
            ps.setString(2, name);
            ps.execute();
            ps.close();
            System.out.println("Your account was successfully deleted");

        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while deleting account", e);
        }
    }

    public void listAccs() throws UnsuccessfulCommandExecutionExc {
        checkLogin();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select\n" +
                    "    a.name,\n" +
                    "    a.balance\n" +
                    "from account as a\n" +
                    "where a.user_id = '" + currentUser.getId() + "';");

            while (rs.next()) {
                System.out.println(
                        rs.getString("name") + ", " +
                                rs.getBigDecimal("balance")
                );
            }

            statement.close();
        } catch (SQLException e) {
            throw new UnsuccessfulCommandExecutionExc("Exception while listing accs", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected");
            } catch (SQLException ignored) {}
        }
    }

    private void checkLogin() throws UnsuccessfulCommandExecutionExc {
        if (currentUser.getEmail() == null) {
            throw new UnsuccessfulCommandExecutionExc("You must log in");
        }
    }
}

class CurrentUser {

    private String email;
    private int id;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
