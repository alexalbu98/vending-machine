package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ChangeProductQuantityState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public String getStateMessage() {
    return "Changing product quantity\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type <product code>=<quantity> to change its quantity:");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    vendingMachine.updateProductQuantity(getProductCode(input), getProductQuantity(input));
    vendingMachine.setState(new ResetState(vendingMachine));
  }

  private int getProductCode(String input) {
    return Integer.parseInt(input.split("=")[0].trim());
  }

  private Integer getProductQuantity(String input) {
    return Integer.parseInt(input.split("=")[1].trim());
  }
}
