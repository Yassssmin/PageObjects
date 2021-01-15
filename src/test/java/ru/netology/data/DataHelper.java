package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya","qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Data
    @AllArgsConstructor
    public static class CardInfo {
        private String number;
        private int balance;
        private String nominal;
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001", 10000, "RUB");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002", 10000, "RUB");
    }

    public static String getRandomCardNumber() {
        Faker faker = new Faker();

        return faker.business().creditCardNumber();
    }

    public static int getRandomAmount(int cardBalance) {
        Random random = new Random();

        return random.nextInt(cardBalance);
    }

    public static int getInvalidAmount(int cardBalance) {
        Random random = new Random();

        return cardBalance + random.nextInt();
    }

    public static void transferMoney(CardInfo from, CardInfo to, int amount) {
        int fromBalance = from.getBalance() - amount;
        int toBalance = to.getBalance() + amount;

        from.setBalance(fromBalance);
        to.setBalance(toBalance);
    }
}
