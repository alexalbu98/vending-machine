package me.alex.vendingmachine.domain.product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductSystem implements ProductSystem {

  private final Integer availableProduct;
  private final Map<Integer, ProductInventory> products;

  public InMemoryProductSystem(Integer availableProduct) {
    this.availableProduct = availableProduct;
    this.products = new HashMap<>();
  }

  public void addProductInventory(int position, ProductInventory productInventory) {
    if (position <= 0 || position > availableProduct) {
      throw new IllegalArgumentException(
          "Product system position out of bounds! Available slots from 1 to "
              + availableProduct);
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
