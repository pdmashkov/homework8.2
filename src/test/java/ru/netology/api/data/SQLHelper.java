package ru.netology.api.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getCode(DataHelper.AuthInfo authInfo) {
        String code;

        String id = getIdByLogin(authInfo.getLogin());

        var dataSQL = "select code from auth_codes where user_id = ?";
        try (var conn = getConnection()) {
            code = runner.query(conn, dataSQL, id, new ScalarHandler<>());
        }

        return code;
    }

    @SneakyThrows
    public static String getIdByLogin(String login) {
        String id;

        var dataSQL = "select id from users where login = ?";
        try (var conn = getConnection()) {
            id = runner.query(conn, dataSQL, login, new ScalarHandler<>());
        }

        return id;
    }

    @SneakyThrows
    public static void deleteCode(String code) {
        var dataSQL = "delete from auth_codes where code = ?";
        try (var conn = getConnection()) {
            runner.update(conn, dataSQL, code);
        }
    }
}
