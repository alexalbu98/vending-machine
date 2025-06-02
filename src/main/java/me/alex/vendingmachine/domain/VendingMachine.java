package me.alex.vendingmachine.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.change.ChangeStore;
import me.alex.vendingmachine.domain.coin.CoinReader;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.VendingMachineState;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VendingMachine {

  private VendingMachineState currentState;
  private final ProductSystem productSystem;
  private final CoinReader coinReader;
  private final ChangeStore changeStore;

  public void setState(VendingMachineState newState) {
    this.currentState = newState;
  }

  public void addProductInventory(int position, ProductInventory productInventory) {
    productSystem.addProductInventory(position, productInventory);
  }

  public List<String> getAvailableOptions() {
    return currentState.getAvailableOptions();
  }

  public void doAction(String input) {
    currentState.doAction(input);
  }

  public void insertCoin(String coin) {
    var readCoin = coinReader.readCoin(coin);
    changeStore.incrementChange(readCoin.name());
  }
}
