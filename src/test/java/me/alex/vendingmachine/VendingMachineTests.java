package me.alex.vendingmachine;

import static me.alex.vendingmachine.domain.coin.CoinFactory.dime;
import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static me.alex.vendingmachine.domain.product.ProductFactory.water;
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
import me.alex.vendingmachine.domain.payment.CardPaymentResult;
import me.alex.vendingmachine.domain.payment.CreditCardDetails;
import me.alex.vendingmachine.domain.payment.PaymentProcessorClient;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.product.ProductInventorySnapshot;
import me.alex.vendingmachine.domain.product.ProductSystem;
import me.alex.vendingmachine.domain.state.VendingMachineState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendingMachineTests {

  VendingMachine vendingMachine;
  ProductSystem productSystem;
  VendingMachineState currentState = mock(VendingMachineState.class);
  CoinReader coinReader;
  ChangeStore changeStore;
  PaymentProcessorClient paymentProcessorClient;

  private final List<ProductInventory> productInventory = List.of(
      new ProductInventory(coke(), 10, 10, 60),
      new ProductInventory(pepsi(), 10, 10, 61),
      new ProductInventory(water(), 10, 0, 62)
  );

  private final CreditCardDetails creditCardDetails = new CreditCardDetails("1234567890123456",
      "alex", "12/25", "123");

  @BeforeEach
  void setupMocks() {
    productSystem = mock(ProductSystem.class);
    currentState = mock(VendingMachineState.class);
    coinReader = mock(CoinReader.class);
    changeStore = mock(ChangeStore.class);
    paymentProcessorClient = mock(PaymentProcessorClient.class);

    when(productSystem.getProductInventory()).thenReturn(productInventory);
    when(productSystem.getProductInventory(60)).thenReturn(productInventory.get(0));
    when(productSystem.getProductInventory(61)).thenReturn(productInventory.get(1));
    when(productSystem.getProductInventory(62)).thenReturn(productInventory.get(2));

    vendingMachine = VendingMachine.builder()
        .productSystem(productSystem)
        .coinReader(coinReader)
        .changeStore(changeStore)
        .currentState(currentState)
        .currentCredit(new BigDecimal("2.00"))
        .paymentProcessorClient(paymentProcessorClient)
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
  void inputActionDelegatesToCurrentInput() {
    vendingMachine.inputAction("Input");
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
    vendingMachine.payProduct(60);
    assertEquals(new BigDecimal("0.50"), vendingMachine.getCurrentCredit());
  }

  @Test
  void getAvailableProductsReturnsProductListFromProductSystem() {
    List<ProductInventory> result = vendingMachine.getAvailableProducts();
    assertEquals(productInventory.stream().map(ProductInventorySnapshot::from).toList(), result);
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
    assertThrows(IllegalStateException.class, () -> vendingMachine.verifyProductQuantity("62"));
  }

  @Test
  void verifyProductQuantityDoesNotThrowExceptionWhenProductIsInStock() {
    assertDoesNotThrow(() -> vendingMachine.verifyProductQuantity("60"));
  }

  @Test
  void verifyEnoughCreditToBuyThrowsExceptionForInvalidProductCode() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of());

    assertThrows(IllegalArgumentException.class,
        () -> vendingMachine.verifyEnoughCreditToBuy("999"));
  }

  @Test
  void verifyEnoughCreditToBuyThrowsExceptionWhenCreditIsInsufficient() {
    vendingMachine.setCurrentCredit(new BigDecimal("0.50"));
    assertThrows(IllegalStateException.class, () -> vendingMachine.verifyEnoughCreditToBuy("60"));
  }

  @Test
  void verifyEnoughCreditToBuyDoesNotThrowWhenCreditIsSufficient() {
    assertDoesNotThrow(() -> vendingMachine.verifyEnoughCreditToBuy("60"));
  }

  @Test
  void productCodeExistsReturnsTrueForExistingProductCode() {
    assertTrue(vendingMachine.productCodeExists("60"));
  }

  @Test
  void productCodeExistsReturnsFalseForNonExistingProductCode() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of());

    assertFalse(vendingMachine.productCodeExists("999"));
  }

  @Test
  void processCardPaymentReturnsSuccessfulResultForValidPayment() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getProduct()).thenReturn(coke());
    when(productSystem.getProductInventory(1)).thenReturn(productInventory);
    when(paymentProcessorClient.requestPayment(creditCardDetails, coke().price()))
        .thenReturn(new CardPaymentResult(true, "success"));

    CardPaymentResult result = vendingMachine.processCardPayment(creditCardDetails, "1");
    assertEquals(new CardPaymentResult(true, "success"), result);
  }

  @Test
  void processCardPaymentThrowsExceptionForInvalidProductCode() {
    when(productSystem.getProductInventory(99)).thenThrow(
        new IllegalArgumentException("Invalid product code"));

    assertThrows(IllegalArgumentException.class,
        () -> vendingMachine.processCardPayment(creditCardDetails, "99"));
  }

  @Test
  void processCardPaymentReturnsFailureForDeclinedPayment() {
    ProductInventory productInventory = mock(ProductInventory.class);
    when(productInventory.getProduct()).thenReturn(coke());
    when(productSystem.getProductInventory(1)).thenReturn(productInventory);
    when(paymentProcessorClient.requestPayment(creditCardDetails, coke().price()))
        .thenReturn(new CardPaymentResult(false, "declined"));

    CardPaymentResult result = vendingMachine.processCardPayment(creditCardDetails, "1");
    assertEquals(new CardPaymentResult(false, "declined"), result);
  }

  @Test
  void canAcceptInputReturnsTrueWhenStateAllowsInput() {
    when(currentState.canAcceptInput()).thenReturn(true);
    assertTrue(vendingMachine.canAcceptInput());
  }

  @Test
  void canAcceptInputReturnsFalseWhenStateDoesNotAllowInput() {
    when(currentState.canAcceptInput()).thenReturn(false);
    assertFalse(vendingMachine.canAcceptInput());
  }
}
