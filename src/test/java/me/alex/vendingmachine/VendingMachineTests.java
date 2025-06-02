package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.change.ChangeStore;
import me.alex.vendingmachine.domain.coin.CoinReader;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.VendingMachineState;
import org.junit.jupiter.api.Test;

public class VendingMachineTests {

  @Test
  void setStateUpdatesCurrentState() {
    VendingMachineState initialState = mock(VendingMachineState.class);
    VendingMachineState newState = mock(VendingMachineState.class);
    VendingMachine vendingMachine = VendingMachine.builder()
        .productSystem(mock(ProductSystem.class))
        .coinReader(mock(CoinReader.class))
        .changeStore(mock(ChangeStore.class))
        .currentState(initialState)
        .build();

    vendingMachine.setState(newState);
    assertEquals(newState, vendingMachine.getCurrentState());
  }

  @Test
  void addProductInventoryAddsProductToSystem() {
    ProductSystem productSystem = mock(ProductSystem.class);
    ProductInventory productInventory = mock(ProductInventory.class);
    VendingMachine vendingMachine = VendingMachine.builder()
        .productSystem(productSystem)
        .coinReader(mock(CoinReader.class))
        .changeStore(mock(ChangeStore.class))
        .build();

    vendingMachine.addProductInventory(1, productInventory);
    verify(productSystem).addProductInventory(1, productInventory);
  }

  @Test
  void getAvailableOptionsReturnsOptionsFromCurrentState() {
    VendingMachineState currentState = mock(VendingMachineState.class);
    VendingMachine vendingMachine = VendingMachine.builder()
        .productSystem(mock(ProductSystem.class))
        .coinReader(mock(CoinReader.class))
        .changeStore(mock(ChangeStore.class))
        .currentState(currentState)
        .build();

    when(currentState.getAvailableOptions()).thenReturn(List.of("Option1", "Option2"));
    List<String> options = vendingMachine.getAvailableOptions();
    assertEquals(List.of("Option1", "Option2"), options);
  }

  @Test
  void doActionDelegatesToCurrentState() {
    VendingMachineState currentState = mock(VendingMachineState.class);
    VendingMachine vendingMachine = VendingMachine.builder()
        .productSystem(mock(ProductSystem.class))
        .coinReader(mock(CoinReader.class))
        .changeStore(mock(ChangeStore.class))
        .currentState(currentState)
        .build();

    vendingMachine.doAction("Input");
    verify(currentState).doAction("Input");
  }
}
