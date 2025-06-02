package me.alex.vendingmachine.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductSystem {

  void addProductInventory(int position, ProductInventory productInventory);

  Optional<ProductInventory> getProductInventory(int position);

  public List<ProductInventory> getProductInventory();

}
