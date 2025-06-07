package me.alex.vendingmachine.domain.state.card;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.payment.CreditCardDetails;
import me.alex.vendingmachine.domain.state.VendingMachineState;

@RequiredArgsConstructor
public class ReadCardDetailsState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String productCode;
  private final CreditCardDetails creditCardDetails;

  @Override
  public String getStateMessage() {
    return "Please enter card details!";
  }

  @Override
  public List<String> getAvailableOptions() {
    if (creditCardDetails.cardNumber() == null || creditCardDetails.cardNumber().isEmpty()) {
      return List.of("Please enter card number:");
    }
    if (creditCardDetails.cardHolderName() == null || creditCardDetails.cardHolderName()
        .isEmpty()) {
      return List.of("Please enter cardholder name:");
    }
    if (creditCardDetails.expiryDate() == null || creditCardDetails.expiryDate().isEmpty()) {
      return List.of("Please enter card expiry date (MM/YY):");
    }
    if (creditCardDetails.cvv() == null || creditCardDetails.cvv().isEmpty()) {
      return List.of("Please enter card CVV:");
    }
    return List.of();
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (creditCardDetails.cardNumber() == null || creditCardDetails.cardNumber().isEmpty()) {
      verifyCardNumber(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode,
              creditCardDetails.setCardNumber(input)));
      return;
    }
    if (creditCardDetails.cardHolderName() == null || creditCardDetails.cardHolderName()
        .isEmpty()) {
      verifyCardHolderName(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode,
              creditCardDetails.setCardHolderName(input)));
      return;
    }
    if (creditCardDetails.expiryDate() == null || creditCardDetails.expiryDate().isEmpty()) {
      verifyExpiryDate(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode,
              creditCardDetails.setExpiryDate(input)));
      return;
    }
    if (creditCardDetails.cvv() == null || creditCardDetails.cvv().isEmpty()) {
      verifyCVV(input);
      vendingMachine.setState(
          new ProcessingPaymentState(vendingMachine, creditCardDetails.setCvv(input), productCode));
    }
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }

  private void verifyCardNumber(String cardNumber) {
    if (!cardNumber.matches("\\d+")) {
      throw new IllegalArgumentException("Invalid card number format. Please enter only digits.");
    }
  }

  private void verifyCardHolderName(String cardHolderName) {
    if (!cardHolderName.matches("[a-zA-Z ]+")) {
      throw new IllegalArgumentException(
          "Invalid cardholder name format. Use letters and spaces only.");
    }
  }

  private void verifyExpiryDate(String expiryDate) {
    if (!expiryDate.matches("^(0[1-9]|1[0-2])/([0-9]{2})$")) {
      throw new IllegalArgumentException("Invalid expiry date format. Use MM/YY.");
    }
  }

  private void verifyCVV(String cvv) {
    if (!cvv.matches("\\d{3}")) {
      throw new IllegalArgumentException("Invalid CVV format. Please enter a 3-digit number.");
    }
  }
}
