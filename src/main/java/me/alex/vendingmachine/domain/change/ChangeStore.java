package me.alex.vendingmachine.domain.change;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import me.alex.vendingmachine.domain.coin.Coin;

public interface ChangeStore {

  void addChange(Coin coin, int quantity);

  void incrementChange(Coin coin);

  void decrementChange(Coin coin);

  int getChangeQuantity(Coin coin);

  List<Change> refund(BigDecimal sum);

  boolean isLowOnChange();

  Map<Coin, Integer> getCoins();

  void removeCoins();

  void removeCoin(Coin coin, int coinQuantity);
}
