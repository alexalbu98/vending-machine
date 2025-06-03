package me.alex.vendingmachine.domain.change;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.alex.vendingmachine.domain.coin.Coin;

public class BigCoinsFirstRefundPolicy implements RefundPolicy {

  @Override
  public List<Change> refund(Map<Coin, Integer> coinQuantities, BigDecimal sum) {
    if (sum.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Sum must be non-negative");
    }

    List<Coin> sortedCoins = sortCoins(coinQuantities.keySet());

    BigDecimal remaining = sum;
    List<Change> changes = new ArrayList<>();

    for (Coin coin : sortedCoins) {
      int remainingQuantity = coinQuantities.getOrDefault(coin, 0);
      int usedQuantity = 0;
      while (remainingQuantity > 0 && remaining.compareTo(BigDecimal.ZERO) > 0) {
        if (remaining.compareTo(coin.value()) >= 0) {
          remaining = remaining.subtract(coin.value());
          usedQuantity++;
          remainingQuantity--;
        } else {
          break;
        }
      }
      if (usedQuantity > 0) {
        changes.add(new Change(coin, usedQuantity));
      }
    }

    return changes;
  }

  private List<Coin> sortCoins(Set<Coin> coins) {
    return coins.stream()
        .sorted((c1, c2) -> c2.value().compareTo(c1.value()))
        .toList();
  }
}
