package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Setter
    @Data
    @AllArgsConstructor
    public static class CardInfo {
        private String number;
        private String balance;
        private String nominal;
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001", "10000", "RUB");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002", "10000", "RUB");
    }

    public static CardInfo getInvalidCardInfo() {
        return new CardInfo("5559 0000 0000 0003", "10000", "RUB");
    }

    public static String getMaskedCardNumber(CardInfo cardInfo) {
        return cardInfo.getNumber().replaceAll("^\\d{4} \\d{4} \\d{4}", "**** **** ****");
    }

    public static int getRandomAmount(CardInfo cardInfo) {
        Random random = new Random();

        return random.nextInt(Integer.parseInt(cardInfo.getBalance()));
    }

    public static int getInvalidAmount(CardInfo cardInfo) {
        Random random = new Random();

        return Integer.parseInt(cardInfo.getBalance()) + random.nextInt();
    }

    public static int getInvalidAmountZero(CardInfo cardInfo) {

        return 0;
    }

    public static void transferMoney(CardInfo from, CardInfo to, int amount) {
        int fromBalance = Integer.parseInt(from.getBalance()) - amount;
        int toBalance = Integer.parseInt(to.getBalance()) + amount;

        from.setBalance(String.valueOf(fromBalance));
        to.setBalance(String.valueOf(toBalance));
    }

    public static Collection<String> toMaskedCardsPrint(Collection<CardInfo> cards) {
        Collection<String> maskedCards = new ArrayList<>();

        for (CardInfo card : cards) {
            String maskedNumber = maskedNumber(card.getNumber());

            String maskedCard = String.format("%s, баланс: %s р.", maskedNumber, card.getBalance());

            maskedCards.add(maskedCard);
        }

        return maskedCards;
    }

    private static String maskedNumber(String number) {
        return "**** ".repeat(3) + number.substring(15);
    }
}
