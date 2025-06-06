package me.alex.vendingmachine.domain.state;

import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.reset.ResetState;

public abstract class ResettableState implements VendingMachineState {

  protected final VendingMachine vendingMachine;
  protected static String RESET_COMMAND = "reset";

  protected ResettableState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(RESET_COMMAND)) {
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    notResetAction(input);
  }

  abstract protected void notResetAction(String input);
}
