package me.alex.vendingmachine.domain.product;

import lombok.Getter;

@Getter
public class ProductInventory {

  private final Product product;
  private final int maxQuantity;
  private int quantity;
  private final Integer code;

  public ProductInventory(Product product, int maxQuantity, Integer code) {
    verifyArguments(quantity, maxQuantity, code);
    this.product = product;
    this.maxQuantity = maxQuantity;
    this.code = code;
  }

  public ProductInventory(Product product, int maxQuantity, int quantity, Integer code) {
    verifyArguments(quantity, maxQuantity, code);
    this.product = product;
    this.maxQuantity = maxQuantity;
    this.code = code;
    this.quantity = quantity;
  }

  private void verifyArguments(int quantity, int maxQuantity, int code) {
    if (maxQuantity <= 0) {
      throw new IllegalArgumentException("Maximum quantity must be greater than zero");
    }
    if (quantity < 0 || quantity > maxQuantity) {
      throw new IllegalArgumentException(
          "Quantity must be greater or equal to zero and less than or equal to maximum quantity: "
              + maxQuantity);
    }
    if (code < 10) {
      throw new IllegalArgumentException("Product code must be greater than or equal to 10");
    }
  }

  public void increaseQuantity(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount to increase product quantity cannot be negative");
    }
    if (this.quantity + amount > maxQuantity) {
      throw new IllegalArgumentException(
          "Cannot increase product quantity beyond maximum available space: " + maxQuantity);
    }
    this.quantity += amount;
  }

  public void increaseQuantity() {
    if (this.quantity + 1 > maxQuantity) {
      throw new IllegalArgumentException(
          "Cannot increase product quantity beyond maximum available space: " + maxQuantity);
    }
    this.quantity++;
  }

  public void decreaseQuantity() {
    if (this.quantity == 0) {
      throw new IllegalStateException("Cannot decrease product quantity below zero");
    }
    this.quantity--;
  }

  public int getQuantity() {
    return this.quantity;
  }
}
