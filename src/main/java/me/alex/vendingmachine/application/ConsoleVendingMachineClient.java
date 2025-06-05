package me.alex.vendingmachine.application;

import java.util.Scanner;
import me.alex.vendingmachine.domain.AbstractVendingMachineClient;
import me.alex.vendingmachine.domain.VendingMachine;

public class ConsoleVendingMachineClient extends AbstractVendingMachineClient {

  private final Scanner scanner = new Scanner(System.in);

  public ConsoleVendingMachineClient(VendingMachine vendingMachine) {
    super(vendingMachine);
  }

  @Override
  public void awaitInput() {
    var input = readConsoleInput();
    try {
      vendingMachine.inputAction(input);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void stateAction() {
    try {
      String result = vendingMachine.stateAction();
      System.out.println(result);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void showInfo() {
    displayStateMessage();
    displayOptions();
  }

  private void displayStateMessage() {
    System.out.println(vendingMachine.getStateMessage());
  }

  private void displayOptions() {
    System.out.println("\nAvailable options:");
    var options = vendingMachine.getAvailableOptions();
    options.forEach(System.out::println);
  }

  private String readConsoleInput() {
    return scanner.nextLine();
  }
}
