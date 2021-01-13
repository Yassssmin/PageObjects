package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RefillPage {
    private SelenideElement button = $("[data-test-id=action-transfer]");

    public DashboardPage fillInfo(String cardNumber, Integer amount) {
        $("[data-test-id=amount] input").setValue(String.valueOf(amount));
        $("[data-test-id=from] input").setValue(cardNumber);

        button.click();

        return new DashboardPage();
    }

    public static class Error {
        private static SelenideElement error = $("[data-test-id=error-notification]");

        public static void checkError() { error.shouldBe(text("Ошибка! Произошла ошибка")); }
    }
}
