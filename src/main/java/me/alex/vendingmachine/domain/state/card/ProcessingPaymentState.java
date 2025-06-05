package me.alex.vendingmachine.domain.state.card;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.DispensingState;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.VendingMachineState;

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
      vendingMachine.setState(
          new DispensingState(vendingMachine, Integer.parseInt(productCode), false));
    } else {
      vendingMachine.setState(new IdleState(vendingMachine));
    }
    return result.message();
  }

  @Override
  public void inputAction(String input) {

  }

  @Override
  public boolean canAcceptInput() {
    return false;
  }
}
