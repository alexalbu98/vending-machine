package me.alex.vendingmachine.domain.state;

public interface VendingMachineState {

  void waitForInput();
  void displayStatus();
  void displayOffer();
}
