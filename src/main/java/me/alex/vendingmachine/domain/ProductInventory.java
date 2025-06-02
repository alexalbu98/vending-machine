package me.alex.vendingmachine.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductInventory {

  private final Product product;
  private final int maxQuantity;
  private int quantity;

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
    this.quantity++;
  }

  public void decreaseQuantity() {
    if (this.quantity == 0) {
      throw new IllegalStateException("Cannot decrease product quantity below zero");
    }
    this.quantity--;
  }
}
