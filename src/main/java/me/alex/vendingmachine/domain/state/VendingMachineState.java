package me.alex.vendingmachine.domain.state;

import java.util.List;
import java.util.stream.Collectors;
import me.alex.vendingmachine.domain.VendingMachine;

public interface VendingMachineState {

  public static final String RESET_COMMAND = "reset";

  String getStateMessage();

  List<String> getAvailableOptions();

  String stateAction();

  void inputAction(String input);

  default String formatProductsAsString(VendingMachine vendingMachine) {
    return vendingMachine
        .getAvailableProducts().stream()
        .map(
            p -> p.getCode() + " - " + p.getProduct().name() + " - price: " + p.getProduct().price()
                + ", quantity: "
                + p.getQuantity()).collect(Collectors.joining("\n"));
  }
}
