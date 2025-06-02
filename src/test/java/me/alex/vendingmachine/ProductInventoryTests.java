package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.product.Product;
import me.alex.vendingmachine.domain.product.ProductInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductInventoryTests {

  ProductInventory inventory;
  Product product;

  @BeforeEach
  void initObjects() {
    product = new Product("Soda", new BigDecimal("1.50"));
    inventory = new ProductInventory(product, 10, 60);
  }


  @Test
  void increaseQuantityIncreasesBySpecifiedAmount() {
    inventory.increaseQuantity(5);
    assertEquals(5, inventory.getQuantity());
  }

  @Test
  void increaseQuantityThrowsExceptionWhenAmountIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> inventory.increaseQuantity(-1));
  }

  @Test
  void increaseQuantityThrowsExceptionWhenExceedingMaxQuantity() {
    inventory.increaseQuantity(8);
    assertThrows(IllegalArgumentException.class, () -> inventory.increaseQuantity(3));
  }

  @Test
  void increaseQuantityByOneIncreasesQuantity() {
    inventory.increaseQuantity();
    assertEquals(1, inventory.getQuantity());
  }

  @Test
  void decreaseQuantityDecreasesByOne() {
    inventory.increaseQuantity(5);
    inventory.decreaseQuantity();
    assertEquals(4, inventory.getQuantity());
  }

  @Test
  void decreaseQuantityThrowsExceptionWhenQuantityIsZero() {
    assertThrows(IllegalStateException.class, inventory::decreaseQuantity);
  }
}
