package ru.netology.api.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.api.data.ApiHelper;
import ru.netology.api.data.DataHelper;
import ru.netology.api.data.SQLHelper;

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

        ApiHelper.transferMoney(transferMoney, token);

        int actualFirstCardBalance = DataHelper.getCardBalance(token, "0") - amount;
        Assertions.assertEquals(balanceFirstCard, actualFirstCardBalance);

        int actualSecondCardBalance = DataHelper.getCardBalance(token, "1") + amount;
        Assertions.assertEquals(balanceSecondCard, actualSecondCardBalance);
    }

    @Test
    public void shouldSuccessTransferSecondToFirst() {
        int amount = DataHelper.getAmount(balanceSecondCard);

        DataHelper.TransferMoney transferMoney = new DataHelper.TransferMoney(secondCardNumber, firstCardNumber, amount);

        ApiHelper.transferMoney(transferMoney, token);

        int actualFirstCardBalance = DataHelper.getCardBalance(token, "0") + amount;
        Assertions.assertEquals(balanceFirstCard, actualFirstCardBalance);

        int actualSecondCardBalance = DataHelper.getCardBalance(token, "1") - amount;
        Assertions.assertEquals(balanceSecondCard, actualSecondCardBalance);
    }

    @AfterAll
    public static void tearDown() {
        SQLHelper.deleteTestData();
    }
}
