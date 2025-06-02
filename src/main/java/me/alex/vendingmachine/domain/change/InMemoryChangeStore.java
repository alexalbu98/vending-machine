package me.alex.vendingmachine.domain.change;

import java.util.HashMap;
import java.util.Map;

public class InMemoryChangeStore implements ChangeStore {

  private final Map<String, Integer> changeMap = new HashMap<>();


  public void addChange(String coinName, int quantity) {
    changeMap.put(coinName, changeMap.getOrDefault(coinName, 0) + quantity);
  }

  public void incrementChange(String coinName) {
    changeMap.put(coinName, changeMap.getOrDefault(coinName, 0) + 1);
  }

  public void decrementChange(String coinName) {
    if (!changeMap.containsKey(coinName) || changeMap.get(coinName) == 0) {
      throw new IllegalStateException("Cannot decrement change for " + coinName + " below zero");
    }
    changeMap.put(coinName, changeMap.get(coinName) - 1);
  }

  public int getChangeQuantity(String coinName) {
    if (coinName == null || !changeMap.containsKey(coinName)) {
      throw new IllegalArgumentException(
          "Coin name cannot be null and it must exist in the change storage");
    }
    return changeMap.getOrDefault(coinName, 0);
  }

}
