package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.RefillPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMoneyTransfer {
    public DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCards() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = DataHelper.getRandomAmount(secondCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        val updatedDashboardPage = refillPage.fillInfo(secondCardInfo.getNumber(), amount);

        DataHelper.transferMoney(secondCardInfo, firstCardInfo, amount);

        assertEquals(updatedDashboardPage.getCardBalance(firstCardInfo.getNumber()), firstCardInfo.getBalance());
        assertEquals(updatedDashboardPage.getCardBalance(secondCardInfo.getNumber()), secondCardInfo.getBalance());
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCards() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        val updatedDashboardPage = refillPage.fillInfo(firstCardInfo.getNumber(), amount);

        DataHelper.transferMoney(firstCardInfo, secondCardInfo, amount);

        assertEquals(updatedDashboardPage.getCardBalance(firstCardInfo.getNumber()), firstCardInfo.getBalance());
        assertEquals(updatedDashboardPage.getCardBalance(secondCardInfo.getNumber()), secondCardInfo.getBalance());
    }

    @Test
    void shouldNotTransferMoneyWithEmptyFields() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        refillPage.fillInfo(null, null);

        RefillPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyWithInvalidCardNumber() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));

        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        refillPage.fillInfo(DataHelper.getRandomCardNumber(), amount);

        RefillPage.checkError();
    }

    //    Баги
    /*   Тут принимается сумма превышающая лимит карты,
   должна выдаватся ошибка, что недостаточно средств на карте, доступно (сумма на карте) */
    @Test
    void shouldNotTransferMoneyFromSecondToFirstInvalidAmount() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = DataHelper.getInvalidAmount(secondCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        refillPage.fillInfo(secondCardInfo.getNumber(), amount);

        RefillPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromFirstToSecondInvalidAmount() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = DataHelper.getInvalidAmount(firstCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        refillPage.fillInfo(firstCardInfo.getNumber(), amount);

        RefillPage.checkError();
    }

    /* Тут принимаеться нулевая сумма, должна выдаватся ошибка,
        что сумма не может быть равной 0 */
    @Test
    void shouldNotTransferMoneyFromSecondToFirstInvalidAmountZero() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = 0;

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        refillPage.fillInfo(secondCardInfo.getNumber(), amount);

        RefillPage.checkError();
    }

    @Test
    void shouldNotTransferMoneyFromFirstToSecondInvalidAmountZero() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));
        val secondCardInfo = DataHelper.getSecondCardInfo();
        secondCardInfo.setBalance(dashboardPage.getCardBalance(secondCardInfo.getNumber()));

        val amount = 0;

        val refillPage = dashboardPage.selectCard(secondCardInfo.getNumber());
        refillPage.fillInfo(firstCardInfo.getNumber(), amount);

        RefillPage.checkError();
    }

    /* Тут осуществяется перевод с карты на эту же карту, должна выдаваться ошибка,
        что невозможно осуществить перевод с карты на эту же карту */
    @Test
    void shouldNotTransferMoneyFromTheSameCard() {
        val firstCardInfo = DataHelper.getFirstCardInfo();
        firstCardInfo.setBalance(dashboardPage.getCardBalance(firstCardInfo.getNumber()));

        val amount = DataHelper.getRandomAmount(firstCardInfo.getBalance());

        val refillPage = dashboardPage.selectCard(firstCardInfo.getNumber());
        refillPage.fillInfo(firstCardInfo.getNumber(), amount);

        RefillPage.checkError();
    }
}
