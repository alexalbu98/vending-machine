package me.alex.vendingmachine;

import java.math.BigDecimal;

public enum Products {
  COKE("1.50"),

  PEPSI("1.45"),

  WATER("0.90");

  private final BigDecimal price;

  Products(String price) {
    this.price = new BigDecimal(price);
  }

  public BigDecimal getPrice() {
    return price;
  }

}
