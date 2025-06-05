package me.alex.vendingmachine;

import static me.alex.vendingmachine.domain.coin.CoinFactory.dime;
import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.VendingMachineType;
import me.alex.vendingmachine.domain.change.Change;
import me.alex.vendingmachine.domain.change.ChangeStore;
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
    verify(currentState).inputAction("Input");
  }

  @Test
  void updateCreditUpdatesCredit() {
    vendingMachine.incrementCredit("2.10");
    assertEquals(new BigDecimal("4.10"), vendingMachine.getCurrentCredit());
  }

  @Test
  void insertCoinUpdatesAvailableChange() {
    when(coinReader.readCoin("DIME")).thenReturn(dime());
    vendingMachine.insertCoin("DIME");
    verify(changeStore).incrementChange(dime());
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

  @Test
  void refundReturnsCorrectChangeAndResetsCredit() {
    when(changeStore.refund(new BigDecimal("2.00"))).thenReturn(List.of(
        new Change(dime(), 20)
    ));

    List<Change> refundedChange = vendingMachine.refund();

    assertEquals(0, vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO));
    assertEquals(1, refundedChange.size());
    assertEquals(dime(), refundedChange.get(0).coin());
    assertEquals(20, refundedChange.get(0).quantity());
  }

  @Test
  void refundHandlesInsufficientChangeGracefully() {
    when(changeStore.refund(new BigDecimal("2.00"))).thenReturn(List.of());

    List<Change> refundedChange = vendingMachine.refund();

    assertEquals(0, refundedChange.size());
    assertEquals(new BigDecimal("2.00"), vendingMachine.getCurrentCredit());
  }

  @Test
  void resetRestoresDefaultSettings() {
    VendingMachine defaultSettings = VendingMachine.builder()
        .currentCredit(BigDecimal.ZERO)
        .vendingMachineType(VendingMachineType.BEVERAGE)
        .changeStore(mock(ChangeStore.class))
        .productSystem(mock(ProductSystem.class))
        .coinReader(mock(CoinReader.class))
        .build();

    vendingMachine.reset(defaultSettings);

    assertEquals(BigDecimal.ZERO, vendingMachine.getCurrentCredit());
    assertEquals(VendingMachineType.BEVERAGE, vendingMachine.getVendingMachineType());
    assertEquals(defaultSettings.getCoins(), vendingMachine.getCoins());
  }

  @Test
  void insertCoinIncrementsCoinQuantity() {
    when(coinReader.readCoin("DIME")).thenReturn(dime());
    vendingMachine.insertCoin("DIME", 3);
    verify(changeStore, times(3)).incrementChange(dime());
  }

  @Test
  void removeCoinDecrementsSpecificCoinQuantity() {
    when(coinReader.readCoin("DIME")).thenReturn(dime());
    vendingMachine.removeCoin("DIME", 2);
    verify(changeStore).removeCoin(dime(), 2);
  }

  @Test
  void verifyProductQuantityThrowsExceptionForInvalidProductCode() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of());
    assertThrows(IllegalArgumentException.class, () -> vendingMachine.verifyProductQuantity("999"));
  }

  @Test
  void verifyProductQuantityThrowsExceptionWhenProductIsOutOfStock() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getCode()).thenReturn(1);
    when(productInventory.getQuantity()).thenReturn(0);
    when(productInventory.getProduct()).thenReturn(coke());
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(productInventory));

    assertThrows(IllegalStateException.class, () -> vendingMachine.verifyProductQuantity("1"));
  }

  @Test
  void verifyProductQuantityDoesNotThrowExceptionWhenProductIsInStock() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getCode()).thenReturn(1);
    when(productInventory.getQuantity()).thenReturn(5);
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(productInventory));

    assertDoesNotThrow(() -> vendingMachine.verifyProductQuantity("1"));
  }

  @Test
  void verifyEnoughCreditToBuyThrowsExceptionForInvalidProductCode() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of());

    assertThrows(IllegalArgumentException.class,
        () -> vendingMachine.verifyEnoughCreditToBuy("999"));
  }

  @Test
  void verifyEnoughCreditToBuyThrowsExceptionWhenCreditIsInsufficient() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getCode()).thenReturn(1);
    when(productInventory.getProduct()).thenReturn(coke());
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(productInventory));

    vendingMachine.setCurrentCredit(new BigDecimal("0.50"));
    assertThrows(IllegalStateException.class, () -> vendingMachine.verifyEnoughCreditToBuy("1"));
  }

  @Test
  void verifyEnoughCreditToBuyDoesNotThrowWhenCreditIsSufficient() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getCode()).thenReturn(1);
    when(productInventory.getProduct()).thenReturn(coke());
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(productInventory));

    assertDoesNotThrow(() -> vendingMachine.verifyEnoughCreditToBuy("1"));
  }

  @Test
  void productCodeExistsReturnsTrueForExistingProductCode() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getCode()).thenReturn(1);
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(productInventory));

    assertTrue(vendingMachine.productCodeExists("1"));
  }

  @Test
  void productCodeExistsReturnsFalseForNonExistingProductCode() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of());

    assertFalse(vendingMachine.productCodeExists("999"));
  }
}
