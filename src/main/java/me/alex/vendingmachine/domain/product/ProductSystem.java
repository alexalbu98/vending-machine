package me.alex.vendingmachine.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductSystem {

  void addProductInventory(ProductInventory productInventory);

  ProductInventory getProductInventory(int productCode);

  public List<ProductInventory> getProductInventory();

  public void dispenseProduct(int productCode);

}
