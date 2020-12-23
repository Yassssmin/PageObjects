package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Error {
    private SelenideElement error = $("[data-test-id=error-notification]");

    public Error() { error.shouldBe(text("Ошибка! Произошла ошибка")); }
}
