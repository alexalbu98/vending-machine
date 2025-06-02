package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class CoinInsertedState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public String getStateMessage() {
    String message = "Available credit: " + vendingMachine.getCurrentCredit();
    return message + "\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of(
        "Insert more coins! Accepted coins are: " + vendingMachine.getAvailableCoins(),
        "Chose a product by typing its position");
  }

  @Override
  public void doAction(String input) {

  }
}
