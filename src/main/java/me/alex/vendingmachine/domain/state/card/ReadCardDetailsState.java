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
  private final String cardNumber;
  private final String expiryDate;
  private final String cardHolderName;
  private final String cvv;

  @Override
  public String getStateMessage() {
    return "Please enter card details!";
  }

  @Override
  public List<String> getAvailableOptions() {
    if (cardNumber == null || cardNumber.isEmpty()) {
      return List.of("Please enter card number:");
    }
    if (expiryDate == null || expiryDate.isEmpty()) {
      return List.of("Please enter card expiry date (MM/YY):");
    }
    if (cardHolderName == null || cardHolderName.isEmpty()) {
      return List.of("Please enter cardholder name:");
    }
    if (cvv == null || cvv.isEmpty()) {
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
    if (cardNumber == null || cardNumber.isEmpty()) {
      verifyCardNumber(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode, input, expiryDate, cardHolderName,
              cvv));
      return;
    }
    if (expiryDate == null || expiryDate.isEmpty()) {
      verifyExpiryDate(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode, cardNumber, input, cardHolderName,
              cvv));
      return;
    }
    if (cardHolderName == null || cardHolderName.isEmpty()) {
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode, cardNumber, expiryDate, input,
              cvv));
      return;
    }
    if (cvv == null || cvv.isEmpty()) {
      verifyCVV(input);
      vendingMachine.setState(
          new ProcessingPaymentState(vendingMachine,
              new CreditCardDetails(cardNumber, cardHolderName, expiryDate,
                  cvv), productCode));
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
