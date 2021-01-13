package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.RefillPage;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestMoneyTransfer {
    public DashboardPage dashboardPage;
    public ArrayList<DataHelper.CardInfoFull> cards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

        cards.clear();

        val userCards = DataHelper.getCards();

        val cardIds = dashboardPage.getCardsIds();

        for (DataHelper.CardInfo userCard : userCards) {
            val maskedCardNumber = DataHelper.getMaskedCardNumber(userCard);

            val cardId = cardIds.get(maskedCardNumber);
            val cardNumber = userCard.getNumber();
            val cardBalance = dashboardPage.getCardBalance(cardId);

            val cardInfoFull = new DataHelper.CardInfoFull(cardId, cardNumber, cardBalance);

            cards.add(cardInfoFull);
        }
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCards() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = DataHelper.getRandomAmount(secondCard.getBalance());

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(secondCard.getNumber(), amount);

        DataHelper.transferMoney(secondCard, firstCard, amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCards() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = DataHelper.getRandomAmount(firstCard.getBalance());

        val refillPage = dashboardPage.selectCard(secondCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(firstCard.getNumber(), amount);

        DataHelper.transferMoney(firstCard, secondCard, amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }


    //    Баги
    /*   Тут принимается сумма превышающая лимит карты,
   должна выдаватся ошибка, что недостаточно средств на карте, доступно (сумма на карте) */
    @Test
    void shouldTransferMoneyFromSecondToFirstInvalidAmount() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = DataHelper.getInvalidAmount(secondCard.getBalance());

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(secondCard.getNumber(), amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondInvalidAmount() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = DataHelper.getInvalidAmount(firstCard.getBalance());

        val refillPage = dashboardPage.selectCard(secondCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(firstCard.getNumber(), amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    /* Тут принимаеться нулевая сумма, должна выдаватся ошибка,
        что сумма не может быть равной 0 */
    @Test
    void shouldTransferMoneyFromSecondToFirstInvalidAmountZero() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = 0;

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(secondCard.getNumber(), amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondInvalidAmountZero() throws Exception {
        val firstCard = cards.get(0);
        val secondCard = cards.get(1);

        val amount = 0;

        val refillPage = dashboardPage.selectCard(secondCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(firstCard.getNumber(), amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    /* Тут осуществяется перевод с карты на эту же карту, должна выдаваться ошибка,
        что невозможно осуществить перевод с карты на эту же карту */
    @Test
    void shouldTransferMoneyFromFirstToFirstCard() throws Exception {
        val firstCard = cards.get(0);

        val amount = DataHelper.getRandomAmount(firstCard.getBalance());

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        val updatedDashboardPage = refillPage.fillInfo(firstCard.getNumber(), amount);

        assertTrue(updatedDashboardPage.checkCardsBalance(cards));
    }

    @Test
    void shouldNotTransferMoneyWithEmptyFields() throws Exception {
        val firstCard = cards.get(0);

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        refillPage.fillInfo(null, null);

        RefillPage.Error.checkError();
    }

    @Test
    void shouldNotTransferMoneyWithInvalidCardNumber() throws Exception {
        val firstCard = cards.get(0);

        val amount = DataHelper.getRandomAmount(firstCard.getBalance());

        val refillPage = dashboardPage.selectCard(firstCard.getId());
        refillPage.fillInfo(DataHelper.getRandomCardNumber(), amount);

        RefillPage.Error.checkError();
    }
}
