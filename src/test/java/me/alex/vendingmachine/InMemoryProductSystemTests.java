package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;
import me.alex.vendingmachine.domain.product.InMemoryProductSystem;
import me.alex.vendingmachine.domain.product.Product;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryProductSystemTests {

  ProductSystem productSystem;
  ProductInventory inventory;

  @BeforeEach
  void initObjects() {
    productSystem = new InMemoryProductSystem(5);
    inventory = new ProductInventory(
        new Product("Chips", new BigDecimal("2.00")), 10, 60);
  }

  @Test
  void addProductInventoryAddsProductSuccessfully() {
    productSystem.addProductInventory(inventory);
    assertEquals(Optional.of(inventory), productSystem.getProductInventory(60));
  }


  @Test
  void addProductInventoryThrowsExceptionWhenExceedingMaxAllowedProducts() {
    productSystem.addProductInventory(
        new ProductInventory(new Product("Soda", new BigDecimal("1.50")), 5, 61));
    productSystem.addProductInventory(
        new ProductInventory(new Product("Candy", new BigDecimal("1.00")), 5, 62));
    productSystem.addProductInventory(
        new ProductInventory(new Product("Water", new BigDecimal("1.20")), 5, 63));
    productSystem.addProductInventory(
        new ProductInventory(new Product("Juice", new BigDecimal("2.50")), 5, 64));
    productSystem.addProductInventory(
        new ProductInventory(new Product("Gum", new BigDecimal("0.50")), 5, 65));

    assertThrows(IllegalStateException.class, () -> productSystem.addProductInventory(
        new ProductInventory(new Product("Cookies", new BigDecimal("3.00")), 5, 66)));
  }

  @Test
  void getProductInventoryReturnsEmptyForNonExistentPosition() {
    assertEquals(Optional.empty(), productSystem.getProductInventory(99));
  }

  @Test
  void getProductInventoryReturnsCorrectInventoryForExistingPosition() {
    productSystem.addProductInventory(inventory);
    assertEquals(Optional.of(inventory), productSystem.getProductInventory(60));
  }

  @Test
  void getProductInventoryListReturnsAllAddedInventories() {
    ProductInventory inventory2 = new ProductInventory(new Product("Soda", new BigDecimal("1.50")),
        5, 61);
    productSystem.addProductInventory(inventory);
    productSystem.addProductInventory(inventory2);

    assertEquals(2, productSystem.getProductInventory().size());
    assertTrue(productSystem.getProductInventory().contains(inventory));
    assertTrue(productSystem.getProductInventory().contains(inventory2));
  }
}
