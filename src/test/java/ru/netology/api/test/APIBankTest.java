package ru.netology.api.test;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.api.data.DataHelper;
import ru.netology.api.data.Specification;

import static io.restassured.RestAssured.given;

public class APIBankTest {
    private String token;
    private String firstCardNumber;
    private String secondCardNumber;
    private int balanceFirstCard;
    private int balanceSecondCard;

    @BeforeEach
    public void setUp() {
        var authInfo = DataHelper.authInfo();

        token = DataHelper.verificationCode(authInfo);

        firstCardNumber = DataHelper.getFirstCard();
        secondCardNumber = DataHelper.getSecondCard();

        balanceFirstCard = DataHelper.getCardBalance(token, "0");
        balanceSecondCard = DataHelper.getCardBalance(token, "1");
    }

    @Test
    public void shouldSuccessTransferFirstToSecond() {
        int amount = DataHelper.getAmount(balanceFirstCard);

        DataHelper.TransferMoney transferMoney = new DataHelper.TransferMoney(firstCardNumber, secondCardNumber, amount);

        given()
                .spec(Specification.requestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(transferMoney)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);

        int actualFirstCardBalance = DataHelper.getCardBalance(token, "0") - amount;
        Assertions.assertEquals(balanceFirstCard, actualFirstCardBalance);

        int actualSecondCardBalance = DataHelper.getCardBalance(token, "1") + amount;
        Assertions.assertEquals(balanceSecondCard, actualSecondCardBalance);
    }

    @Test
    public void shouldSuccessTransferSecondToFirst() {
        int amount = DataHelper.getAmount(balanceSecondCard);

        DataHelper.TransferMoney transferMoney = new DataHelper.TransferMoney(secondCardNumber, firstCardNumber, amount);

        given()
                .spec(Specification.requestSpecification())
                .header("Authorization", "Bearer " + token)
                .body(transferMoney)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);

        int actualFirstCardBalance = DataHelper.getCardBalance(token, "0") + amount;
        Assertions.assertEquals(balanceFirstCard, actualFirstCardBalance);

        int actualSecondCardBalance = DataHelper.getCardBalance(token, "1") - amount;
        Assertions.assertEquals(balanceSecondCard, actualSecondCardBalance);
    }
}
