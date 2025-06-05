package me.alex.vendingmachine;

import me.alex.vendingmachine.application.ConsoleVendingMachineClient;
import me.alex.vendingmachine.domain.VendingMachineFactory;

public class Main {

  public static void main(String[] args) {
    var vm = VendingMachineFactory.beverageVendingMachine();
    var consoleClient = new ConsoleVendingMachineClient(vm);
    consoleClient.start();
  }
}
