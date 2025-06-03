package me.alex.vendingmachine.domain.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryProductSystem implements ProductSystem {

  private final Integer maxAllowedProducts;
  private final Map<Integer, ProductInventory> products;

  public InMemoryProductSystem(Integer maxAllowedProducts) {
    this.maxAllowedProducts = maxAllowedProducts;
    this.products = new HashMap<>();
  }

  public void addProductInventory(ProductInventory productInventory) {
    if (products.size() + 1 > maxAllowedProducts) {
      throw new IllegalStateException(
          "Cannot add more products than available product system slots: " + maxAllowedProducts);
    }
    if (products.containsKey(productInventory.getCode())) {
      throw new IllegalArgumentException("Product with code "
          + productInventory.getCode() + " already exists in the system.");
    }
    products.put(productInventory.getCode(), productInventory);
  }

  public ProductInventory getProductInventory(int productCode) {
    if (products.containsKey(productCode)) {
      return products.get(productCode);
    }
    throw new IllegalArgumentException("Product with code " + productCode + " not found.");
  }

  public List<ProductInventory> getProductInventory() {
    return products.values().stream().toList();
  }

  @Override
  public void dispenseProduct(int productCode) {
    var inventory = getProductInventory(productCode);
    inventory.decreaseQuantity();
  }
}
