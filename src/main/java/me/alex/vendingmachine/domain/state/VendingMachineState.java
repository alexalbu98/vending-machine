package me.alex.vendingmachine.domain.state;

import java.util.List;
import java.util.stream.Collectors;
import me.alex.vendingmachine.domain.VendingMachine;

public interface VendingMachineState {

  String getStateMessage();

  List<String> getAvailableOptions();

  void doAction(String input);

  default String formatProductsAsString(VendingMachine vendingMachine) {
    return vendingMachine
        .getAvailableProducts().stream()
        .map(p -> p.getProduct().name() + " - price: " + p.getProduct().price() + ", quantity: "
            + p.getQuantity()).collect(Collectors.joining("\n"));
  }
}
