package me.alex.vendingmachine;

import java.math.BigDecimal;

public enum Coins {
  NICKEL("0.05", 100),
  DIME("0.10", 100),
  FIFTY_CENT("0.50", 100),
  COIN("1.00", 100),
  TWO_COIN("2.00", 100);


  private final BigDecimal value;
  private int initialQuantity;

  Coins(String value, int initialQuantity) {
    this.value = new BigDecimal(value);
    this.initialQuantity = initialQuantity;
  }

  public void increaseQuantity() {
    this.initialQuantity++;
  }

  public boolean decreaseQuantity() {
    if (this.initialQuantity == 0) {
      return false;
    }
    initialQuantity--;
    return true;
  }

  public BigDecimal getValue() {
    return value;
  }
}
