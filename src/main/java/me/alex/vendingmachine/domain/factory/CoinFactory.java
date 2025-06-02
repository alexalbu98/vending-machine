package me.alex.vendingmachine.domain.factory;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.Coin;

public class CoinFactory {

  public static Coin nickel() {
    return new Coin("Nickel", new BigDecimal("0.05"));
  }

  public static Coin dime() {
    return new Coin("Dime", new BigDecimal("0.10"));
  }

  public static Coin halfCoin() {
    return new Coin("Half Coin", new BigDecimal("0.50"));
  }

  public static Coin coin() {
    return new Coin("Coin", new BigDecimal("1.00"));
  }

  public static Coin twoCoin() {
    return new Coin("Two Coin", new BigDecimal("2.00"));
  }
}
