package me.alex.vendingmachine.domain.state;

import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class IdleState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Insert coins! Accepted coins are 0.05, 0.10, 0.50, 1.00, 2.00");
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
  public void doAction() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter a string: ");
    String input = scanner.nextLine();
    System.out.println("You entered: " + input);
    scanner.close();
  }
}
