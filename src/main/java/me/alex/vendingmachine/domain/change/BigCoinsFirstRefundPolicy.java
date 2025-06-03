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
      int quantity = coinQuantities.getOrDefault(coin, 0);
      while (quantity > 0 && remaining.compareTo(BigDecimal.ZERO) > 0) {
        if (remaining.compareTo(coin.value()) >= 0) {
          changes.add(new Change(coin, 1));
          remaining = remaining.subtract(coin.value());
          quantity--;
        } else {
          break;
        }
      }
    }

    if (remaining.compareTo(BigDecimal.ZERO) > 0) {
      throw new IllegalStateException("Cannot return exact change for the given sum!");
    }

    return changes;
  }

  private List<Coin> sortCoins(Set<Coin> coins) {
    return coins.stream()
        .sorted((c1, c2) -> c2.value().compareTo(c1.value()))
        .toList();
  }
}
