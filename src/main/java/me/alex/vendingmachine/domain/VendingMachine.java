package me.alex.vendingmachine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.coin.CoinReader;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.state.VendingMachineState;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VendingMachine {

  private VendingMachineState initialState;
  private final ProductSystem productSystem;
  private final CoinReader coinReader;

  public void changeState(VendingMachineState newState) {
    this.initialState = newState;
  }

  //TODO define vending machine actions

}
