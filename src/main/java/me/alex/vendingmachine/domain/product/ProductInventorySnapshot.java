package me.alex.vendingmachine.domain.product;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ProductInventorySnapshot extends ProductInventory {

  public ProductInventorySnapshot(Product product, int maxQuantity, Integer code) {
    super(product, maxQuantity, code);
  }

  public ProductInventorySnapshot(Product product, int maxQuantity, int quantity, Integer code) {
    super(product, maxQuantity, quantity, code);
  }

  public static ProductInventorySnapshot from(ProductInventory productInventory) {
    return new ProductInventorySnapshot(
        productInventory.getProduct(),
        productInventory.getMaxQuantity(),
        productInventory.getQuantity(),
        productInventory.getCode());
  }

  @Override
  public void increaseQuantity(int amount) {
    throw new UnsupportedOperationException(
        "Cannot increase quantity of a snapshot product inventory");
  }

  @Override
  public void increaseQuantity() {
    throw new UnsupportedOperationException(
        "Cannot increase quantity of a snapshot product inventory");
  }

  @Override
  public void decreaseQuantity() {
    throw new UnsupportedOperationException(
        "Cannot decrease quantity of a snapshot product inventory");
  }

  @Override
  public void setProduct(Product product) {
    throw new UnsupportedOperationException("Cannot set product of a snapshot product inventory");
  }

  @Override
  public void setQuantity(int quantity) {
    throw new UnsupportedOperationException("Cannot set quantity of a snapshot product inventory");
  }

}
