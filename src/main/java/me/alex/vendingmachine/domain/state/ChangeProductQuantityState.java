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
    return List.of("Type <product code>=<quantity> to change its quantity");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {

  }
}
