package ru.netology.page;

import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.WebElement;
import ru.netology.data.DataHelper;

import java.util.ArrayList;
import java.util.Collection;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public RefillPage selectCard(DataHelper.CardInfo cardInfo) {
        val cardMaskedNumber = DataHelper.getMaskedCardNumber(cardInfo);

        val cardElement = $(Selectors.withText(cardMaskedNumber));

        cardElement.$("button[data-test-id=action-deposit]").click();

        return new RefillPage();
    }

    public boolean checkCardsBalance(Collection<DataHelper.CardInfo> cards) throws Exception {
        ElementsCollection elements = Selenide.$$(".list__item>div");

        Collection<String> maskedCards = DataHelper.toMaskedCardsPrint(cards);

        Collection<String> pageCards = new ArrayList<>();

        for (WebElement webElement : elements) {
            String pageCard = webElement.getText().replaceAll("\\s+Пополнить", "");

            pageCards.add(pageCard);
        }

        System.out.print(pageCards);

        System.out.print(maskedCards);

        return pageCards.containsAll(maskedCards);
    }
}
