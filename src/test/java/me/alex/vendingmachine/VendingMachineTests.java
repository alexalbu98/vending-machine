package me.alex.vendingmachine;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.change.ChangeStore;
import me.alex.vendingmachine.domain.coin.Coin;
import me.alex.vendingmachine.domain.coin.CoinReader;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.VendingMachineState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendingMachineTests {

  VendingMachine vendingMachine;
  ProductSystem productSystem;
  ProductInventory productInventory;
  VendingMachineState currentState = mock(VendingMachineState.class);
  CoinReader coinReader;
  ChangeStore changeStore;

  @BeforeEach
  void setupMocks() {
    productSystem = mock(ProductSystem.class);
    productInventory = mock(ProductInventory.class);
    currentState = mock(VendingMachineState.class);
    coinReader = mock(CoinReader.class);
    changeStore = mock(ChangeStore.class);

    vendingMachine = VendingMachine.builder()
        .productSystem(productSystem)
        .coinReader(coinReader)
        .changeStore(changeStore)
        .currentState(currentState)
        .currentCredit(new BigDecimal("2.00"))
        .build();

  }

  @Test
  void setStateUpdatesCurrentState() {
    VendingMachineState newState = mock(VendingMachineState.class);

    vendingMachine.setState(newState);
    assertEquals(newState, vendingMachine.getCurrentState());
  }

  @Test
  void addProductInventoryAddsProductToSystem() {
    ProductInventory newInventory = mock(ProductInventory.class);

    vendingMachine.addProductInventory(newInventory);
    verify(productSystem).addProductInventory(newInventory);
  }

  @Test
  void getAvailableOptionsReturnsOptionsFromCurrentState() {
    when(currentState.getAvailableOptions()).thenReturn(List.of("Option1", "Option2"));
    List<String> options = vendingMachine.getAvailableOptions();
    assertEquals(List.of("Option1", "Option2"), options);
  }

  @Test
  void doActionDelegatesToCurrentState() {
    vendingMachine.doAction("Input");
    verify(currentState).doAction("Input");
  }

  @Test
  void insertCoinUpdatesCreditAndIncrementsChangeStore() {
    when(coinReader.readCoin("QUARTER")).thenReturn(new Coin("QUARTER", new BigDecimal("0.25")));
    vendingMachine.insertCoin("QUARTER");
    verify(changeStore).incrementChange("QUARTER");
    assertEquals(new BigDecimal("2.25"), vendingMachine.getCurrentCredit());
  }

  @Test
  void dispenseProductThrowsExceptionWhenProductCodeIsInvalid() {
    doThrow(new IllegalArgumentException("Invalid product code")).when(
        productSystem).dispenseProduct(99);

    assertThrows(IllegalArgumentException.class, () -> vendingMachine.dispenseProduct(99));
  }

  @Test
  void updateCreditSubtractsProductPriceFromCurrentCredit() {
    when(productSystem.getProductInventory(1)).thenReturn(productInventory);
    when(productInventory.getProduct()).thenReturn(coke());

    vendingMachine.payProduct(1);

    assertEquals(new BigDecimal("0.50"), vendingMachine.getCurrentCredit());
  }

  @Test
  void getAvailableProductsReturnsProductListFromProductSystem() {
    List<ProductInventory> products = List.of(mock(ProductInventory.class),
        mock(ProductInventory.class));
    when(productSystem.getProductInventory()).thenReturn(products);

    List<ProductInventory> result = vendingMachine.getAvailableProducts();

    assertEquals(products, result);
  }
}
