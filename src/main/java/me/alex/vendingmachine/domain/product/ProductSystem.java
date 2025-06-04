package me.alex.vendingmachine.domain.product;

import java.util.List;

public interface ProductSystem {

  void addProductInventory(ProductInventory productInventory);

  ProductInventory getProductInventory(int productCode);

  List<ProductInventory> getProductInventory();

  void dispenseProduct(int productCode);
}
