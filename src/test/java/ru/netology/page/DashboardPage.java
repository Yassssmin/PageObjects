package ru.netology.page;

import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.WebElement;
import ru.netology.data.DataHelper;

import java.util.Collection;
import java.util.HashMap;

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

    public HashMap<String, String> getCardsIds() {
        HashMap<String, String> cardIds = new HashMap<>();

        for (WebElement card : cards) {
            String cardId = card.getAttribute("data-test-id");
            String cardNumber = getCardNumber(cardId);
            cardIds.put(cardNumber, cardId);
        }

        return cardIds;
    }

    public int getCardBalance(String id) {
        for (WebElement card : cards) {
            if (card.getAttribute("data-test-id").equals(id)) {
                return extractBalance(card.getText());
            }
        }

        return 0;
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public String getCardNumber(String id) {
        for (WebElement card : cards) {
            if (card.getAttribute("data-test-id").equals(id)) {
                return extractNumber(card.getText());
            }
        }

        return "";
    }

    private String extractNumber(String text) {
        return text.substring(0, numberLength);
    }

    public RefillPage selectCard(String id) {
        val cardElement = $(".list__item>div[data-test-id='" + id + "']");

        cardElement.$("button[data-test-id=action-deposit]").click();

        return new RefillPage();
    }

    public boolean checkCardsBalance(Collection<DataHelper.CardInfoFull> cards) throws Exception {
        for (DataHelper.CardInfoFull card : cards) {
            int cardBalance = getCardBalance(card.getId());

            if (cardBalance != card.getBalance()) {
                return false;
            }
        }

        return true;
    }
}
