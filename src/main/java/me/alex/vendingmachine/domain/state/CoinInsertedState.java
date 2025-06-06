package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.reset.ResetState;

@RequiredArgsConstructor
public class CoinInsertedState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String REFUND = "R";

  @Override
  public String getStateMessage() {
    String message = "Available credit: " + vendingMachine.getCurrentCredit();
    if (vendingMachine.isLowOnChange()) {
      message += "\nWarning: Vending machine is low on change! Use fix money or pay by card!\n";
    }
    return message + "\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of(
        "Insert more coins! Accepted coins are: " + vendingMachine.getAvailableCoins(),
        "Chose a product by typing its code",
        "Type 'R' for refund"
    );
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (vendingMachine.productCodeExists(input)) {
      vendingMachine.verifyEnoughCreditToBuy(input);
      vendingMachine.verifyProductQuantity(input);
      vendingMachine.setState(new DispensingState(vendingMachine, Integer.valueOf(input), true));
      return;
    }
    if (input.equals(REFUND)) {
      vendingMachine.setState(new RefundingState(vendingMachine));
      return;
    }
    if (input.equals(RESET_COMMAND)) {
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    vendingMachine.insertCoin(input);
    vendingMachine.incrementCredit(input);
  }

  @Override
  public boolean canAcceptInput() {
    return true;
  }

}