package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMoneyTransfer {
    public ArrayList<DataHelper.CardInfo> cards;

    @BeforeAll
    void setUp() {
        cards = new ArrayList<>();

        cards.add(DataHelper.getFirstCardInfo());
        cards.add(DataHelper.getSecondCardInfo());
    }

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCards() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getRandomAmount(secondCard);
        
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(firstCard);
        val updatedDashboardPage = refillPage.fillInfo(secondCard, amount);

        DataHelper.transferMoney(secondCard, firstCard, amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCards() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getRandomAmount(firstCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(secondCard);
        val updatedDashboardPage = refillPage.fillInfo(firstCard, amount);

        DataHelper.transferMoney(firstCard, secondCard, amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }


//    Баги
    /*   Тут принимаеться сумма превышающая лимит карты,
   должна выдаватся ошибка, что недостаточно средств на карте, доступно (сумма на карте) */
    @Test
    void shouldTransferMoneyFromSecondToFirstInvalid() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getInvalidAmount(secondCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(firstCard);
        val updatedDashboardPage = refillPage.fillInfo(secondCard, amount);


        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondInvalid() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getInvalidAmount(firstCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(secondCard);
        val updatedDashboardPage = refillPage.fillInfo(firstCard, amount);


        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }
    /* Тут принимаеться нулевая сумма, должна выдаватся ошибка,
        что сумма не может быть равной 0 */
    @Test
    void shouldTransferMoneyFromSecondToFirstInvalidAmauntZero() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getInvalidAmountZero(secondCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(firstCard);
        val updatedDashboardPage = refillPage.fillInfo(secondCard, amount);


        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondInvalidAmauntZero() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);
        val amount = DataHelper.getInvalidAmountZero(firstCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(secondCard);
        val updatedDashboardPage = refillPage.fillInfo(firstCard, amount);


        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }
    /* Тут осуществяется перевод с карты на эту же карту, должна выдаваться ошибка,
        что невозможно осуществить перевод с карты на эту же карту */
    @Test
    void shouldTransferMoneyFromFirstToFirstCard() throws Exception {
        val firstCard = cards.get(0);
        val amount = DataHelper.getRandomAmount(firstCard);

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val refillPage = dashboardPage.selectCard(firstCard);
        val updatedDashboardPage = refillPage.fillInfo(firstCard, amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }
}
