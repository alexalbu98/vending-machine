package me.alex.vendingmachine;

import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.state.VendingMachineState;

@RequiredArgsConstructor
public class VendingMachine {
  private final VendingMachineState initialState;

  public void changeState(VendingMachineState newState) {
  }
}
