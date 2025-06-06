package me.alex.vendingmachine.domain.state;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;

public class CoinInsertedState extends ResettableState {

  private static final String REFUND = "R";

  public CoinInsertedState(VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public String getStateMessage() {
    String message = "Available credit: " + vendingMachine.getCurrentCredit();
    if (vendingMachine.isLowOnChange()) {
      message += "\nWarning: Vending machine is low on change! Use fix money or pay by card!\n";
    }
    return message + "\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of(
        "Insert more coins! Accepted coins are: " + vendingMachine.getAvailableCoins(),
        "Chose a product by typing its code",
        "Type 'R' for refund"
    );
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  protected void notResetAction(String input) {
    if (vendingMachine.productCodeExists(input)) {
      vendingMachine.verifyEnoughCreditToBuy(input);
      vendingMachine.verifyProductQuantity(input);
      vendingMachine.setState(new DispensingState(vendingMachine, Integer.valueOf(input), true));
      return;
    }
    if (input.equals(REFUND)) {
      vendingMachine.setState(new RefundingState(vendingMachine));
      return;
    }
    vendingMachine.insertCoin(input);
    vendingMachine.incrementCredit(input);
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }

}