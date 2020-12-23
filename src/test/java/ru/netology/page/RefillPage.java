package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import javax.xml.validation.Validator;

import static com.codeborne.selenide.Selenide.$;

public class RefillPage {
    private SelenideElement Button = $("[data-test-id=action-transfer]");

    public DashboardPage fillInfo(DataHelper.CardInfo cardInfo, int amount) {
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").setValue(cardInfo.getNumber());

        Button.click();

        return new DashboardPage();
    }

}
