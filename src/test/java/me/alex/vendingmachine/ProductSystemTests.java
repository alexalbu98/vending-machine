package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import me.alex.vendingmachine.domain.product.Product;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductSystemTests {

  ProductSystem productSystem;
  ProductInventory inventory;

  @BeforeEach
  void initObjects() {
    productSystem = new ProductSystem(5);
    inventory = new ProductInventory(
        new Product("Chips", new BigDecimal("2.00")), 10);
  }

  @Test
  void addProductInventoryAddsProductToValidPosition() {
    productSystem.addProductInventory(3, inventory);
    assertEquals(Optional.of(inventory), productSystem.getProductInventory(3));
  }

  @Test
  void addProductInventoryThrowsExceptionForInvalidPosition() {
    assertThrows(IllegalArgumentException.class,
        () -> productSystem.addProductInventory(6, inventory));
  }

  @Test
  void getProductInventoryReturnsEmptyForNonExistentPosition() {
    assertEquals(Optional.empty(), productSystem.getProductInventory(2));
  }

  @Test
  void getProductInventoryReturnsEmptyForPositionOutOfBounds() {
    assertEquals(Optional.empty(), productSystem.getProductInventory(0));
  }
}
