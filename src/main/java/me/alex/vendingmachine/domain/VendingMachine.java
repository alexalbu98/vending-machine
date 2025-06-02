package me.alex.vendingmachine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import me.alex.vendingmachine.state.VendingMachineState;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendingMachine {

  private VendingMachineState initialState;
  private ProductSystem productSystem;

  public void changeState(VendingMachineState newState) {
    this.initialState = newState;
  }

  //TODO define vending machine actions

}
