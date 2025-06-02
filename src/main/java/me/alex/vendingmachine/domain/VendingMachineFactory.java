package me.alex.vendingmachine.domain;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static me.alex.vendingmachine.domain.product.ProductFactory.water;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.change.InMemoryChangeStore;
import me.alex.vendingmachine.domain.coin.FiveCoinsReader;
import me.alex.vendingmachine.domain.product.InMemoryProductSystem;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.IdleState;

public class VendingMachineFactory {

  public static VendingMachine beverageVendingMachine() {
    var vm = VendingMachine.builder()
        .coinReader(new FiveCoinsReader())
        .changeStore(new InMemoryChangeStore())
        .productSystem(new InMemoryProductSystem(3))
        .currentCredit(BigDecimal.ZERO)
        .build();

    vm.addProductInventory(1, new ProductInventory(pepsi(), 10, 10));
    vm.addProductInventory(2, new ProductInventory(coke(), 10, 10));
    vm.addProductInventory(3, new ProductInventory(water(), 10, 10));

    vm.setState(new IdleState(vm));
    return vm;
  }

}
