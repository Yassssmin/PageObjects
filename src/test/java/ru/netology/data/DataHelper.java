package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.util.ArrayList;
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
        private String balance;
        private String nominal;
    }

    @Setter
    @Data
    @AllArgsConstructor
    public static class CardInfoFull {
        private String id;
        private String number;
        private int balance;
    }

    public static ArrayList<CardInfo> getCards() {
        ArrayList<CardInfo> cards = new ArrayList<>();

        cards.add(new CardInfo("5559 0000 0000 0001", "10000", "RUB"));
        cards.add(new CardInfo("5559 0000 0000 0002", "10000", "RUB"));

        return cards;
    }

    public static String getRandomCardNumber() {
        Faker faker = new Faker();

        return faker.business().creditCardNumber();
    }

    public static String getMaskedCardNumber(CardInfo cardInfo) {
        return cardInfo.getNumber().replaceAll("^\\d{4} \\d{4} \\d{4}", "**** **** ****");
    }

    public static int getRandomAmount(int cardBalance) {
        Random random = new Random();

        return random.nextInt(cardBalance);
    }

    public static int getInvalidAmount(int cardBalance) {
        Random random = new Random();

        return cardBalance + random.nextInt();
    }

    public static void transferMoney(CardInfoFull from, CardInfoFull to, int amount) {
        int fromBalance = from.getBalance() - amount;
        int toBalance = to.getBalance() + amount;

        from.setBalance(fromBalance);
        to.setBalance(toBalance);
    }
}
