package me.alex.vendingmachine.domain.state;

import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class IdleState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public void waitForInput() {

  }

  @Override
  public void displayStatus() {

  }

  @Override
  public void displayOffer() {

  }
}
