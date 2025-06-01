package me.alex.vendingmachine;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.state.VendingMachineState;

@AllArgsConstructor
@NoArgsConstructor
public class VendingMachine {
  private VendingMachineState initialState;

  public void changeState(VendingMachineState newState) {
    this.initialState = newState;
  }
}
