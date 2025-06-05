package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ChangeProductQuantityState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final static String EXIT = "exit";

  @Override
  public String getStateMessage() {
    return "Changing product quantity\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type <product code>=<quantity> to change its quantity:",
        "Type 'exit' to return back");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(EXIT)) {
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    vendingMachine.updateProductQuantity(getProductCode(input), getProductQuantity(input));
    vendingMachine.setState(new ResetState(vendingMachine));
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }

  private int getProductCode(String input) {
    return Integer.parseInt(input.split("=")[0].trim());
  }

  private Integer getProductQuantity(String input) {
    var args = input.split("=");
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Invalid input format. Expected format: <product code>=<quantity>");
    }
    return Integer.parseInt(args[1].trim());
  }
}
