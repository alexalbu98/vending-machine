package me.alex.vendingmachine.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductSystem {

  private final Integer availableSlots;
  private final Map<Integer, ProductInventory> products;

  public ProductSystem(Integer availableSlots) {
    this.availableSlots = availableSlots;
    this.products = new HashMap<>();
  }

  public void addProductInventory(int position, ProductInventory productInventory) {
    if (position <= 0 || position > availableSlots) {
      throw new IllegalArgumentException(
          "Product system position out of bounds! Available slots from 1 to " + availableSlots);
    }
    products.put(position, productInventory);
  }

  public Optional<ProductInventory> getProductInventory(int position) {
    if (products.containsKey(position)) {
      return Optional.ofNullable(products.get(position));
    }
    return Optional.empty();
  }

}
