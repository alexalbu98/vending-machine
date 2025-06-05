package me.alex.vendingmachine.domain.state.card;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.VendingMachineState;

@RequiredArgsConstructor
public class CardPaymentState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final static String EXIT = "exit";

  @Override
  public String getStateMessage() {
    return "Paying by card\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of(
        "Chose a product by typing its code",
        "Type 'exit' to cancel"
    );
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(EXIT)) {
      vendingMachine.setState(new IdleState(vendingMachine));
      return;
    }
    if (vendingMachine.productCodeExists(input)) {
      vendingMachine.verifyProductQuantity(input);
      vendingMachine.setState(new ReadCardDetailsState(vendingMachine, input, null, null, null, null));
      return;
    }
    throw new IllegalArgumentException("Invalid product code: " + input);
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }
}
