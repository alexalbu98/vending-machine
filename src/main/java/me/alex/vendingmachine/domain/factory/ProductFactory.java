package me.alex.vendingmachine.domain.factory;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.Product;

public class ProductFactory {

  public static Product coke() {
    return new Product("Coke", new BigDecimal("1.50"));
  }

  public static Product water() {
    return new Product("Water", new BigDecimal("0.90"));
  }

  public static Product pepsi() {
    return new Product("Pepsi", new BigDecimal("1.45"));
  }
}
