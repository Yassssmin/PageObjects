package ru.netology.page;

import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item>div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final int numberLength = 19;

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public String getCardId(String cardNumber) {
        String cardLastFourNumber = cardNumber.substring(15);

        return cards.find(text(cardLastFourNumber)).getAttribute("data-test-id");
    }

    public int getCardBalance(String cardNumber) {
        val cardId = getCardId(cardNumber);

        val cardElement = $(".list__item>div[data-test-id='" + cardId + "']");

        return extractBalance(cardElement.getText());
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public RefillPage selectCard(String cardNumber) {
        val cardId = getCardId(cardNumber);

        val cardElement = $(".list__item>div[data-test-id='" + cardId + "']");

        cardElement.$("button[data-test-id=action-deposit]").click();

        return new RefillPage();
    }
}
