package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ChangeProductPriceState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final static String EXIT = "exit";

  @Override
  public String getStateMessage() {
    return "Changing product price...\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type <product code>=<price> to change its price:",
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
    vendingMachine.updateProductPrice(getProductCode(input), getNewPrice(input));
    vendingMachine.setState(new ResetState(vendingMachine));
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }

  private int getProductCode(String input) {
    return Integer.parseInt(input.split("=")[0].trim());
  }

  private BigDecimal getNewPrice(String input) {
    var args = input.split("=");
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Invalid input format. Expected format: <product code>=<price>");
    }
    return new BigDecimal(args[1].trim());
  }
}
