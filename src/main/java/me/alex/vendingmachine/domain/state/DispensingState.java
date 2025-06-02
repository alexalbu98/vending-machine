package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class DispensingState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final Integer productCode;

  @Override
  public String getStateMessage() {
    return "Dispensing product...";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of();
  }

  @Override
  public void doAction(String input) {

  }
}
