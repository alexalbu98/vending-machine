package me.alex.vendingmachine.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductSystem {

  void addProductInventory(ProductInventory productInventory);

  Optional<ProductInventory> getProductInventory(int productCode);

  public List<ProductInventory> getProductInventory();

}
