package me.alex.vendingmachine.state;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.CoinInsertedState;
import me.alex.vendingmachine.domain.state.DispensingState;
import me.alex.vendingmachine.domain.state.RefundingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoinInsertedStateTests {

  VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(
        new ProductInventory(pepsi(), 10, 10, 60),
        new ProductInventory(coke(), 10, 10, 61)));
  }

  @Test
  void doActionTransitionsToDispensingStateForValidProductCode() {
    doReturn(new BigDecimal("2.00")).when(vendingMachine).getCurrentCredit();
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    state.inputAction("60");

    verify(vendingMachine).setState(any(DispensingState.class));
  }

  @Test
  void inputActionDoesNotTransitionsToDispensingStateWhenInsufficientFunds() {
    doReturn(new BigDecimal("0.05")).when(vendingMachine).getCurrentCredit();
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    assertThrows(IllegalStateException.class, () -> state.inputAction("60"));
  }

  @Test
  void inputActionDoesNotTransitionsToDispensingStateWhenProductOutOfStock() {
    doReturn(new BigDecimal("2.00")).when(vendingMachine).getCurrentCredit();
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(
        new ProductInventory(pepsi(), 10, 0, 60)));
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    assertThrows(IllegalStateException.class, () -> state.inputAction("60"));
  }

  @Test
  void doActionTransitionsToRefundingStateForRefundInput() {
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    state.inputAction("R");
    verify(vendingMachine).setState(any(RefundingState.class));
  }

  @Test
  void doActionInsertsCoinForValidCoinInput() {
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    state.inputAction("QUARTER");
    verify(vendingMachine).insertCoin("QUARTER");
  }

  @Test
  void doActionThrowsExceptionForInvalidProductCode() {
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    doThrow(new IllegalArgumentException("Invalid coin")).when(vendingMachine)
        .insertCoin(anyString());
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("INVALID_COIN"));
  }

  @Test
  void getStateMessageIncludesCreditAndProducts() {
    when(vendingMachine.getCurrentCredit()).thenReturn(new BigDecimal("2.00"));
    CoinInsertedState state = new CoinInsertedState(vendingMachine);

    String message = state.getStateMessage();

    assertTrue(message.contains("Available credit: 2.00"));
    assertTrue(message.contains(coke().name()));
    assertTrue(message.contains(pepsi().name()));
  }

  @Test
  void getAvailableOptionsListsAllValidOptions() {
    when(vendingMachine.getAvailableCoins()).thenReturn(List.of("PENNY, NICKEL, DIME, QUARTER"));
    CoinInsertedState state = new CoinInsertedState(vendingMachine);
    List<String> options = state.getAvailableOptions();
    assertEquals(3, options.size());
    assertTrue(
        options.contains("Insert more coins! Accepted coins are: [PENNY, NICKEL, DIME, QUARTER]"));
    assertTrue(options.contains("Chose a product by typing its code"));
    assertTrue(options.contains("Type 'R' for refund"));
  }
}
