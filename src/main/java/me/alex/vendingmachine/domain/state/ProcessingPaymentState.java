package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class ProcessingPaymentState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String productCode;
  private final String cardNumber;
  private final String expiryDate;
  private final String CVV;

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
    var result = vendingMachine.processCardPayment(productCode, cardNumber, expiryDate, CVV);
    if (result.success()) {
      return result.message() + "\n" + dispenseProduct();
    }
    vendingMachine.setState(new IdleState(vendingMachine));
    return result.message();
  }

  private String dispenseProduct() {
    var state = new DispensingState(vendingMachine, Integer.parseInt(productCode), false);
    try {
      return state.stateAction();
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  @Override
  public void inputAction(String input) {

  }
}
