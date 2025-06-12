package me.alex.vendingmachine.domain.product;

import java.math.BigDecimal;
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

  @Override
  public void updateProductPrice(int productCode, BigDecimal newPrice) {
    if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("New price must be greater than zero.");
    }
    var inventory = products.get(productCode);
    if (inventory == null) {
      throw new IllegalArgumentException("Product with code " + productCode + " not found.");
    }
    var product = inventory.getProduct();
    inventory.setProduct(new Product(product.name(), newPrice));
    products.put(productCode, inventory);
  }

  @Override
  public void updateProductQuantity(int productCode, Integer productQuantity) {
    if (productQuantity <= 0) {
      throw new IllegalArgumentException("New quantity must be greater than zero.");
    }
    var inventory = products.get(productCode);
    if (inventory == null) {
      throw new IllegalArgumentException("Product with code " + productCode + " not found.");
    }
    if(productQuantity > inventory.getMaxQuantity()){
      throw new IllegalArgumentException("Product quantity cannot be above " + inventory.getMaxQuantity());
    }
    inventory.setQuantity(productQuantity);
    products.put(productCode, inventory);
  }
}
