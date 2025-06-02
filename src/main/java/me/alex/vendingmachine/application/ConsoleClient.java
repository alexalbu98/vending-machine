package me.alex.vendingmachine.application;

import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ConsoleClient {

  private final VendingMachine vendingMachine;
  private final Scanner scanner = new Scanner(System.in);

  public void start() {
    displayWelcomeMessage();
    while (true) {
      displayOptions();
      doAction();
      sleep();
    }
  }

  private void doAction() {
    var input = readConsoleInput();
    try {
      vendingMachine.doAction(input);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Thread interrupted: " + e.getMessage());
    }
  }

  private void displayWelcomeMessage() {
    System.out.println("Welcome to the Vending Machine!");
  }

  private void displayOptions() {
    var options = vendingMachine.getAvailableOptions();
    options.forEach(System.out::println);
  }

  private String readConsoleInput() {
    return scanner.nextLine();
  }
}
