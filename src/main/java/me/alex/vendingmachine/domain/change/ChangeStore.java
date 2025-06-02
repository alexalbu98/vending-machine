package me.alex.vendingmachine.domain.change;

public interface ChangeStore {

  void addChange(String coinName, int quantity);

  void incrementChange(String coinName);

  void decrementChange(String coinName);

  int getChangeQuantity(String coinName);
}
