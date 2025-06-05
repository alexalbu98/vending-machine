package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class CoinInsertedState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String REFUND = "R";

  @Override
  public String getStateMessage() {
    String message = "Available credit: " + vendingMachine.getCurrentCredit();
    if (vendingMachine.isLowOnChange()) {
      message += "\nWarning: Vending machine is low on change! Use fix money!\n";
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
    if (isProductCode(input)) {
      verifyCredit(input);
      verifyProductQuantity(input);
      vendingMachine.setState(new DispensingState(vendingMachine, Integer.valueOf(input)));
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

  private void verifyProductQuantity(String productCode) {
    var productInventory = vendingMachine.getAvailableProducts().stream()
        .filter(p -> p.getCode().toString().equals(productCode))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid product code: " + productCode));

    if (productInventory.getQuantity() == 0) {
      throw new IllegalStateException(
          "Product is out of stock: " + productInventory.getProduct().name());
    }
  }

  private boolean isProductCode(String input) {
    return vendingMachine.getAvailableProducts().stream()
        .anyMatch(product -> product.getCode().toString().equals(input));
  }

  private void verifyCredit(String productCode) {
    var productInventory = vendingMachine.getAvailableProducts().stream()
        .filter(p -> p.getCode().toString().equals(productCode))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid product code: " + productCode));
    if (vendingMachine.getCurrentCredit().compareTo(productInventory.getProduct().price()) == -1) {
      throw new IllegalStateException("Insufficient credit. Please insert more coins!");
    }
  }
}