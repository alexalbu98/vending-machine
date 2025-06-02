package me.alex.vendingmachine.domain.product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductSystem {

  private final Integer availableProductSlots;
  private final Map<Integer, ProductInventory> products;

  public ProductSystem(Integer availableProductSlots) {
    this.availableProductSlots = availableProductSlots;
    this.products = new HashMap<>();
  }

  public void addProductInventory(int position, ProductInventory productInventory) {
    if (position <= 0 || position > availableProductSlots) {
      throw new IllegalArgumentException(
          "Product system position out of bounds! Available slots from 1 to " + availableProductSlots);
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
