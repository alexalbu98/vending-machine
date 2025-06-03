package me.alex.vendingmachine.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.change.Change;
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
  private BigDecimal currentCredit;

  public void setState(VendingMachineState newState) {
    this.currentState = newState;
  }

  public String getStateMessage() {
    return currentState.getStateMessage();
  }

  public List<String> getAvailableCoins() {
    return coinReader.availableCoins();
  }

  public BigDecimal getCurrentCredit() {
    return currentCredit;
  }

  public VendingMachineState getCurrentState() {
    return currentState;
  }

  public void addProductInventory(ProductInventory productInventory) {
    productSystem.addProductInventory(productInventory);
  }

  public void dispenseProduct(int productCode) {
    productSystem.dispenseProduct(productCode);
  }

  public void payProduct(int productCode) {
    var productInventory = productSystem.getProductInventory(productCode);
    currentCredit = getCurrentCredit().subtract(productInventory.getProduct().price());
  }

  public List<ProductInventory> getAvailableProducts() {
    return productSystem.getProductInventory();
  }

  public List<String> getAvailableOptions() {
    return currentState.getAvailableOptions();
  }

  public String beforeAction() {
    return currentState.stateAction();
  }

  public void doAction(String input) {
    currentState.inputAction(input);
  }

  public void insertCoin(String coin) {
    var readCoin = coinReader.readCoin(coin);
    changeStore.incrementChange(readCoin);
    this.currentCredit = this.currentCredit.add(readCoin.value());
  }

  public void insertCoin(String coin, int quantity) {
    for (int i = 0; i < quantity; i++) {
      insertCoin(coin);
    }
  }

  public List<Change> refund() {
    var change = changeStore.refund(getCurrentCredit());
    var returnedChange = change.stream()
        .map(c -> c.coin().value().multiply(new BigDecimal(c.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    this.currentCredit = this.currentCredit.subtract(returnedChange);

    return change;
  }
}
