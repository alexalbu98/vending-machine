package me.alex.vendingmachine.domain.state;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.card.CardPaymentState;

public class IdleState extends ResettableState {

  private static final String CARD_PAYMENT = "card";

  public IdleState(VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public String getStateMessage() {
    String message = "Vending machine is ready!\n";
    if (vendingMachine.isLowOnChange()) {
      message += "Warning: Vending machine is low on change! Use fix money or pay by card!\n";
    }
    return message + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of(
        "Insert coins! Accepted coins are: " + vendingMachine.getAvailableCoins(),
        "Type 'card' to pay by credit card");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  protected void notResetAction(String input) {
    if (input.equals(CARD_PAYMENT)) {
      vendingMachine.setState(new CardPaymentState(vendingMachine));
      return;
    }
    vendingMachine.insertCoin(input);
    vendingMachine.incrementCredit(input);
    vendingMachine.setState(new CoinInsertedState(vendingMachine));
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }
}
