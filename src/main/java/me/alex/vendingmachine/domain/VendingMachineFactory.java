package me.alex.vendingmachine.domain;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static me.alex.vendingmachine.domain.product.ProductFactory.water;

import me.alex.vendingmachine.domain.coin.FiveCoinsReader;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.IdleState;

public class VendingMachineFactory {

  public static VendingMachine beverageVendingMachine() {
    var vm = VendingMachine.builder()
        .coinReader(new FiveCoinsReader())
        .changeStore(new ChangeStore())
        .productSystem(new ProductSystem(3))
        .build();

    vm.addProductInventory(1, new ProductInventory(pepsi(), 20, 20));
    vm.addProductInventory(2, new ProductInventory(coke(), 20, 20));
    vm.addProductInventory(3, new ProductInventory(water(), 20, 20));
    vm.setState(new IdleState(vm));

    return vm;
  }

}
