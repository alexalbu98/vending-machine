package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class RefundState implements VendingMachineState{

  private final VendingMachine vendingMachine;

  @Override
  public String getStateMessage() {
    return "";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of();
  }

  @Override
  public String beforeAction() {
    var credit = vendingMachine.getCurrentCredit();
    return "";
  }

  @Override
  public void doAction(String input) {

  }
}
