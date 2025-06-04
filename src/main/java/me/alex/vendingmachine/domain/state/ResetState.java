package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.VendingMachineFactory;

@RequiredArgsConstructor
public class ResetState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final static String DEFAULT = "default";

  @Override
  public String getStateMessage() {
    return "You are in the reset mode";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type 'default' to reset the vending machine to default state",
        "Type 'exit' to exit the reset mode");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(DEFAULT)) {
      var factorySettings = VendingMachineFactory.vendingMachine(
          vendingMachine.getVendingMachineType());
      vendingMachine.reset(factorySettings);
    }
    vendingMachine.setState(new IdleState(vendingMachine));
  }
}
