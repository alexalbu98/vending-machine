package me.alex.vendingmachine.domain.change;

import java.util.Map;
import java.util.TreeMap;
import me.alex.vendingmachine.domain.coin.Coin;

public class InMemoryChangeStore implements ChangeStore {

  private final Map<Coin, Integer> changeMap = new TreeMap<>();


  public void addChange(Coin coin, int quantity) {
    changeMap.put(coin, changeMap.getOrDefault(coin, 0) + quantity);
  }

  public void incrementChange(Coin coin) {
    changeMap.put(coin, changeMap.getOrDefault(coin, 0) + 1);
  }

  public void decrementChange(Coin coin) {
    if (!changeMap.containsKey(coin) || changeMap.get(coin) == 0) {
      throw new IllegalStateException("Cannot decrement change for " + coin.name() + " below zero");
    }
    changeMap.put(coin, changeMap.get(coin) - 1);
  }

  public int getChangeQuantity(Coin coin) {
    if (coin == null || !changeMap.containsKey(coin)) {
      throw new IllegalArgumentException(
          "Coin name cannot be null and it must exist in the change storage");
    }
    return changeMap.getOrDefault(coin, 0);
  }

}
