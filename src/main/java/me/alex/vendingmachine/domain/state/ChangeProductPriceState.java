package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ChangeProductPriceState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public String getStateMessage() {
    return "Changing product price...\n" + formatProductsAsString(vendingMachine);
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type <product code>=<price> to change its price:");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    vendingMachine.updateProductPrice(getProductCode(input), getNewPrice(input));
    vendingMachine.setState(new ResetState(vendingMachine));
  }

  private int getProductCode(String input) {
    return Integer.parseInt(input.split("=")[0].trim());
  }

  private BigDecimal getNewPrice(String input) {
    return new BigDecimal(input.split("=")[1].trim());
  }
}
