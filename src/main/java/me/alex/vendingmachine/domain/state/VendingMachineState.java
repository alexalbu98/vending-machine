package me.alex.vendingmachine.domain.state;

import java.util.List;

public interface VendingMachineState {

  List<String> getAvailableOptions();
  void doAction(String input);
}
