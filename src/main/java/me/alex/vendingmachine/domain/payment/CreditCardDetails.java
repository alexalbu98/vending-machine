package me.alex.vendingmachine.domain.payment;

public record CreditCardDetails(
    String cardNumber,
    String cardHolderName,
    String expiryDate,
    String cvv) {

}

