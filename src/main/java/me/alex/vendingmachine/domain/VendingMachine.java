package me.alex.vendingmachine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.coin.CoinReader;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.VendingMachineState;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VendingMachine {

  private VendingMachineState initialState;
  private final ProductSystem productSystem;
  private final CoinReader coinReader;
  private final ChangeStore changeStore;

  public void setState(VendingMachineState newState) {
    this.initialState = newState;
  }

  public void addProductInventory(int position, ProductInventory productInventory) {
    productSystem.addProductInventory(position, productInventory);
  }

  //TODO define vending machine actions

}
