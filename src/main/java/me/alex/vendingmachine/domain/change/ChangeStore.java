package me.alex.vendingmachine.domain.change;

import me.alex.vendingmachine.domain.coin.Coin;

public interface ChangeStore {

  void addChange(Coin coin, int quantity);

  void incrementChange(Coin coin);

  void decrementChange(Coin coin);

  int getChangeQuantity(Coin coin);
}
