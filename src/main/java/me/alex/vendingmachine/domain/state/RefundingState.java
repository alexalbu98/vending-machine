package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class RefundingState implements VendingMachineState {

  private final VendingMachine VendingMachine;

  @Override
  public String getStateMessage() {
    return "";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of();
  }

  @Override
  public void doAction(String input) {

  }
}
