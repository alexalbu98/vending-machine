package me.alex.vendingmachine.domain.state.reset;

import static me.alex.vendingmachine.domain.state.reset.ResetInputUtils.getProductCode;
import static me.alex.vendingmachine.domain.state.reset.ResetInputUtils.getProductQuantity;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.VendingMachineState;

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
}
