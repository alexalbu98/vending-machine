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
    return List.of("Insert more coins", "Chose a product");
  }

  @Override
  public void doAction(String input) {

  }
}
