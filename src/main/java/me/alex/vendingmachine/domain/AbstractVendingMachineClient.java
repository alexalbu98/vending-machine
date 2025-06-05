package me.alex.vendingmachine.domain;

public abstract class AbstractVendingMachineClient {

  protected final VendingMachine vendingMachine;

  protected AbstractVendingMachineClient(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  public void start() {
    while (true) {
      stateAction();
      if (!vendingMachine.canAcceptInput()) {
        continue;
      }
      showInfo();
      awaitInput();
      sleep();
    }
  }

  public abstract void stateAction();

  public abstract void showInfo();

  public abstract void awaitInput();

  private void sleep() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Thread interrupted: " + e.getMessage());
    }
  }
}
