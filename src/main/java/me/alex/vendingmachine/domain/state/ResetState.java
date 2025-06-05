package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.VendingMachineFactory;

@RequiredArgsConstructor
public class ResetState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final static String DEFAULT = "default";
  private final static String EXIT = "exit";
  private final static String CHANGE_PRICE = "price";
  private final static String CHANGE_QUANTITY = "qty";
  private final static String ADD_COIN = "coin";

  @Override
  public String getStateMessage() {
    return "You are in the reset mode";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type 'default' to reset the vending machine to factory settings",
        "Type 'price' to change product price",
        "Type 'qty' to change product quantity",
        "Type 'coin' to add or remove coins for change",
        "Type 'exit' to exit the reset mode");
  }

  @Override
  public String stateAction() {
    vendingMachine.setCurrentCredit(BigDecimal.ZERO);
    return "";
  }

  @Override
  public void inputAction(String input) {
    switch (input) {
      case DEFAULT -> {
        var factorySettings = VendingMachineFactory.vendingMachine(
            vendingMachine.getVendingMachineType());
        vendingMachine.reset(factorySettings);
        vendingMachine.setState(new IdleState(vendingMachine));
      }
      case EXIT -> vendingMachine.setState(new IdleState(vendingMachine));
      case CHANGE_PRICE -> vendingMachine.setState(new ChangeProductPriceState(vendingMachine));
      case CHANGE_QUANTITY ->
          vendingMachine.setState(new ChangeProductQuantityState(vendingMachine));
      case ADD_COIN -> vendingMachine.setState(new UpdateChangeState(vendingMachine));
      default ->
          throw new IllegalArgumentException("Invalid input: " + input + ". Not a known command.");
    }
  }
}
