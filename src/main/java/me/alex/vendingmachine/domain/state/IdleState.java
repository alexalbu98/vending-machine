package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class IdleState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private static final String CARD_PAYMENT = "card";

  @Override
  public String getStateMessage() {
    String message = "Vending machine is ready!\n";
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
  public void inputAction(String input) {
    if (input.equals(CARD_PAYMENT)) {
      //TODO implement card payment
      return;
    }
    vendingMachine.insertCoin(input);
    vendingMachine.setState(new CoinInsertedState(vendingMachine));
  }
}
