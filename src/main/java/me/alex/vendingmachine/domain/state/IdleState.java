package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class IdleState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Insert coins to start! Accepted coins are 0.05, 0.10, 0.50, 1.00, 2.00",
        "Write 'card' to pay by credit card");
  }

  @Override
  public void displayStatus() {
    System.out.println("Vending machine is idle. ");
  }

  @Override
  public void displayOffer() {
  }

  @Override
  public void displayError() {

  }

  @Override
  public void doAction(String input) {
  }
}
