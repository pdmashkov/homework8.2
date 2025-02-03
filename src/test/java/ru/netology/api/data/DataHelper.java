package ru.netology.api.data;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Value;
import org.apache.http.HttpStatus;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataHelper {
    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo authInfo() {
        AuthInfo authInfo = new AuthInfo("vasya", "qwerty123");

        ApiHelper.authorization(authInfo);

        return authInfo;
    }

    @Value
    public static class VerificationCode {
        private String login;
        private String code;
    }

    public static String verificationCode(AuthInfo authInfo) {
        return ApiHelper.verification(authInfo);
    }

    public static int getCardBalance(String token, String indexCard) {
        Response response = given()
                .spec(Specification.requestSpecification())
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/cards")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        return response.path("[" + indexCard + "].balance");
    }

    public static int getAmount(int balance) {
        Random random = new Random();

        return random.nextInt(balance);
    }

    public static String getFirstCard() {
        return "5559 0000 0000 0001";
    }

    public static String getSecondCard() {
        return "5559 0000 0000 0002";
    }

    @Value
    public static class TransferMoney {
        private String from;
        private String to;
        private int amount;
    }
}
