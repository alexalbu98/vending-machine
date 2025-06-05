package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class DispensingState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final Integer productCode;

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
    String message = "Dispensing selected product... Enjoy!";
    vendingMachine.dispenseProduct(productCode);
    vendingMachine.payProduct(productCode);
    if (vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO) == 0) {
      vendingMachine.setState(new IdleState(vendingMachine));
    } else {
      message = refundChange(message);
      changeStateByCredit();
    }
    return message;
  }

  private void changeStateByCredit() {
    if (vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO) == 0) {
      vendingMachine.setState(new IdleState(vendingMachine));
    } else {
      vendingMachine.setState(new RefundingState(vendingMachine));
    }
  }

  private String refundChange(String message) {
    RefundingState refundingState = new RefundingState(vendingMachine);
    try {
      return message + "\n" + refundingState.stateAction();
    } catch (Exception e) {
      return message + "\n" + e.getMessage();
    }
  }

  @Override
  public void inputAction(String input) {

  }
}
