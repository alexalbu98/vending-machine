package me.alex.vendingmachine.domain.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductSystem implements ProductSystem {

  private Integer maxAllowedProducts;
  private final Map<Integer, ProductInventory> products;

  public InMemoryProductSystem(Integer maxAllowedProducts) {
    this.maxAllowedProducts = maxAllowedProducts;
    this.products = new HashMap<>();
  }

  public void addProductInventory(ProductInventory productInventory) {
    if (products.keySet().size() + 1 > maxAllowedProducts) {
      throw new IllegalStateException(
          "Cannot add more products than available slots: " + maxAllowedProducts);
    }
    products.put(productInventory.getCode(), productInventory);
  }

  public Optional<ProductInventory> getProductInventory(int position) {
    if (products.containsKey(position)) {
      return Optional.ofNullable(products.get(position));
    }
    return Optional.empty();
  }

  public List<ProductInventory> getProductInventory() {
    return products.values().stream().toList();
  }
}
