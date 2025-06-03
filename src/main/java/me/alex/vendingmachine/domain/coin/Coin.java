package me.alex.vendingmachine.domain.coin;

import java.math.BigDecimal;

public record Coin(String name, BigDecimal value) implements Comparable<Coin> {

  @Override
  public int compareTo(Coin coin) {
    return coin.value.compareTo(this.value);
  }
}
