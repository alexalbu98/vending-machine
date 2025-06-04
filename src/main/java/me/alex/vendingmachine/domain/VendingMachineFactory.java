package me.alex.vendingmachine.domain;

import static me.alex.vendingmachine.domain.VendingMachineType.BEVERAGE;
import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static me.alex.vendingmachine.domain.product.ProductFactory.water;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.change.BigCoinsFirstRefundPolicy;
import me.alex.vendingmachine.domain.change.InMemoryChangeStore;
import me.alex.vendingmachine.domain.change.IsChangeLowByCoinNumberAndQuantity;
import me.alex.vendingmachine.domain.coin.FiveCoinsReader;
import me.alex.vendingmachine.domain.product.InMemoryProductSystem;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.IdleState;

public class VendingMachineFactory {

  public static VendingMachine beverageVendingMachine() {
    var vm = VendingMachine.builder()
        .coinReader(new FiveCoinsReader())
        .changeStore(new InMemoryChangeStore(new BigCoinsFirstRefundPolicy(),
            new IsChangeLowByCoinNumberAndQuantity()))
        .productSystem(new InMemoryProductSystem(3))
        .currentCredit(BigDecimal.ZERO)
        .vendingMachineType(BEVERAGE)
        .build();

    vm.addProductInventory(new ProductInventory(pepsi(), 10, 10, 60));
    vm.addProductInventory(new ProductInventory(coke(), 10, 10, 61));
    vm.addProductInventory(new ProductInventory(water(), 10, 10, 62));

    vm.insertCoin("0.05", 10);
    vm.insertCoin("0.10", 9);
    vm.insertCoin("0.50", 8);
    vm.insertCoin("1.00", 7);
    vm.insertCoin("2.00", 6);

    vm.setCurrentCredit(BigDecimal.ZERO);
    vm.setState(new IdleState(vm));

    return vm;
  }

  public static VendingMachine vendingMachine(VendingMachineType type) {
    if (type == VendingMachineType.BEVERAGE) {
      return beverageVendingMachine();
    }
    throw new IllegalArgumentException("Unsupported vending machine type: " + type);
  }

}
