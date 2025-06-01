package me.alex.vendingmachine.state;

public interface VendingMachineState {

  void waitForInput();
  void displayStatus();
  void displayOffer();
}
