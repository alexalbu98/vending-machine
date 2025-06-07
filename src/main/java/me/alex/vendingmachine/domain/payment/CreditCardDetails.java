package me.alex.vendingmachine.domain.payment;

public record CreditCardDetails(
    String cardNumber,
    String cardHolderName,
    String expiryDate,
    String cvv) {

  public CreditCardDetails setCardNumber(String cardNumber) {
    return new CreditCardDetails(cardNumber, this.cardHolderName, this.expiryDate, this.cvv);
  }

  public CreditCardDetails setExpiryDate(String expiryDate) {
    return new CreditCardDetails(this.cardNumber, this.cardHolderName, expiryDate, this.cvv);
  }

  public CreditCardDetails setCardHolderName(String cardHolderName) {
    return new CreditCardDetails(this.cardNumber, cardHolderName, this.expiryDate, this.cvv);
  }

  public CreditCardDetails setCvv(String cvv) {
    return new CreditCardDetails(this.cardNumber, cardHolderName, this.expiryDate, cvv);
  }

  public static CreditCardDetails emptyCardDetails() {
    return new CreditCardDetails(null, null, null, null);
  }
}

