package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ReadCardDetailsState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String productCode;
  private final String cardNumber;
  private final String expiryDate;
  private final String CVV;

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
    if (CVV == null || CVV.isEmpty()) {
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
          new ReadCardDetailsState(vendingMachine, productCode, input, expiryDate, CVV));
      return;
    }
    if (expiryDate == null || expiryDate.isEmpty()) {
      verifyExpiryDate(input);
      vendingMachine.setState(
          new ReadCardDetailsState(vendingMachine, productCode, cardNumber, input, CVV));
      return;
    }
    if (CVV == null || CVV.isEmpty()) {
      verifyCVV(input);
      vendingMachine.setState(
          new ProcessingPaymentState(vendingMachine, productCode, cardNumber, expiryDate, CVV));
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
