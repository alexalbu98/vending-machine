package me.alex.vendingmachine.application;

import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ConsoleClient {

  private final VendingMachine vendingMachine;

  public void start() {
    displayWelcomeMessage();
    displayOptions();
    var input = readConsoleInput();
  }

  private void displayWelcomeMessage() {
    System.out.println("Welcome to the Vending Machine!");
  }

  private void displayOptions() {
    var options = vendingMachine.getAvailableOptions();
    System.out.println("Available options:");
    options.forEach(System.out::println);
  }

  private String readConsoleInput() {
    try (Scanner scanner = new Scanner(System.in)) {
      return scanner.nextLine();
    }
  }
}
