package me.alex.vendingmachine;

import java.math.BigDecimal;

public enum Coins {
  NICKEL("0.05"),
  DIME("0.10"),
  FIFTY_CENT("0.50"),
  COIN("1.00"),
  TWO_COIN("2.00");

  private final BigDecimal value;

  Coins(String value) {
    this.value = new BigDecimal(value);
  }

  public BigDecimal getValue() {
    return value;
  }
}
