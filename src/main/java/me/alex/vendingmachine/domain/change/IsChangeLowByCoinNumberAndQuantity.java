package me.alex.vendingmachine.domain.change;

import java.util.Comparator;
import java.util.Map;
import me.alex.vendingmachine.domain.coin.Coin;

public class IsChangeLowByCoinNumberAndQuantity implements IsChangeLowPolicy {

  private final static int LOW_COIN_NUMBER_LIMIT = 4;

  @Override
  public boolean isChangeLow(Map<Coin, Integer> change) {
    if (change.size() < LOW_COIN_NUMBER_LIMIT) {
      return true;
    }

    var sortedChange = change.entrySet().stream()
        .sorted(Map.Entry.comparingByKey(Comparator.comparing(Coin::value)))
        .toList();

    double lowCoinQuantity = 10;
    for (var entry : sortedChange) {
      if (entry.getValue() < lowCoinQuantity) {
        return true;
      }
      lowCoinQuantity = lowCoinQuantity * 0.8;
    }

    return false;
  }
}
