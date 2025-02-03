package ru.netology.api.data;

import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    public static void authorization(DataHelper.AuthInfo authInfo) {
        given()
                .spec(Specification.requestSpecification())
                .body(authInfo)
                .when()
                .post("/auth")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    public static String verification(DataHelper.AuthInfo authInfo) {
        String code = SQLHelper.getCode(authInfo);

        try {
            DataHelper.VerificationCode verificationCode = new DataHelper.VerificationCode(authInfo.getLogin(), code);

            return given()
                    .spec(Specification.requestSpecification())
                    .body(verificationCode)
                    .when()
                    .post("/auth/verification")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .path("token");
        } finally {
            SQLHelper.deleteCode(code);
        }
    }

    public static void transferMoney(DataHelper.TransferMoney transferMoney, String token) {
        given()
                .spec(Specification.requestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(transferMoney)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
