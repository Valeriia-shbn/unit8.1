package data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Map;

public class DataHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/sut_database";
    private static final String USER = "sut_user";
    private static final String PASSWORD = "sut_password";
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @SneakyThrows
    public static void cleanDB() {
        try (Connection conn = getConnection()) {
            runner.execute(conn, "DELETE FROM auth_codes");
            runner.execute(conn, "DELETE FROM card_transactions");
            runner.execute(conn, "DELETE FROM cards");
            runner.execute(conn, "DELETE FROM users");

        }
    }


    @SneakyThrows
    public static User createUserWithAuthCode(String status) {
        Faker faker = new Faker();
        String userId = faker.internet().uuid();
        String password = faker.internet().password();
        String login = faker.internet().emailAddress();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String authCode = "12345";// String.valueOf(faker.number().randomNumber(7, false));
        try (Connection conn = getConnection()) {

            String insertUserSQL = "INSERT INTO users (id, login, password, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSQL)) {
                userStmt.setString(1, userId);
                userStmt.setString(2, login);
                userStmt.setString(3, hashedPassword);
                userStmt.setString(4, status);
                userStmt.executeUpdate();
            }


            String insertAuthCodeSQL = "INSERT INTO auth_codes (id, user_id, code) VALUES (?, ?, ?)";
            try (PreparedStatement authCodeStmt = conn.prepareStatement(insertAuthCodeSQL)) {
                authCodeStmt.setString(1, faker.internet().uuid());
                authCodeStmt.setString(2, userId);
                authCodeStmt.setString(3, authCode);
                authCodeStmt.executeUpdate();
            }
//            System.out.println("GenCODE " +  authCode);
//            System.out.println("DB CODE " +  getAuthCodeByUserId(userId));

        }
        return new User(userId, login, password, status, getAuthCodeByUserId(userId));
    }

    @SneakyThrows
    private static String getAuthCodeByUserId(String userId) {
        try (Connection conn = getConnection()) {
            String query = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("code");
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
