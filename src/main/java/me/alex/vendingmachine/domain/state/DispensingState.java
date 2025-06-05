package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class DispensingState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final Integer productCode;
  private final boolean isCreditPayment;

  @Override
  public String getStateMessage() {
    return "";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of();
  }

  @Override
  public String stateAction() {
    vendingMachine.dispenseProduct(productCode);
    if (isCreditPayment) {
      vendingMachine.payProduct(productCode);
    }
    if (vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO) == 0) {
      vendingMachine.setState(new IdleState(vendingMachine));
    } else {
      vendingMachine.setState(new RefundingState(vendingMachine));
    }
    return "Dispensing selected product... Enjoy!";
  }

  @Override
  public void inputAction(String input) {

  }

  @Override
  public boolean canAcceptInput() {
    return false;
  }
}
