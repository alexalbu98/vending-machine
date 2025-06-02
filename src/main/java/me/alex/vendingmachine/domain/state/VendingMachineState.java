package me.alex.vendingmachine.domain.state;

import java.util.List;

public interface VendingMachineState {

  List<String> getAvailableOptions();
  void displayStatus();
  void displayOffer();
  void displayError();
  void doAction(String input);
}
