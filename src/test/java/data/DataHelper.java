package data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;

import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class DataHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/sut_database";
    private static final String USER = "sut_user";
    private static final String PASSWORD = "sut_password";
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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
    public static User createUser(String status) {
        Faker faker = new Faker();
        String userId = faker.internet().uuid();
        String password = faker.internet().password();
        String login = faker.internet().emailAddress();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = getConnection()) {
            String insertUserSQL = "INSERT INTO users (id, login, password, status) VALUES (?, ?, ?, ?)";
            runner.update(conn, insertUserSQL, userId, login, hashedPassword, status);
            return new User(userId, login, password, status);
        }
    }

    @SneakyThrows
    public static VerificationCode getVerificationCode(String userId) {
        String query = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1";
        try (Connection conn = getConnection()) {
            String code = runner.query(conn, query, new ScalarHandler<>(), userId);

            return code != null ? new VerificationCode(code) : null;
        }
    }

}

