package me.alex.vendingmachine.state;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.CoinInsertedState;
import me.alex.vendingmachine.domain.state.IdleState;
import org.junit.jupiter.api.Test;

public class IdleStateTests {

  @Test
  void getStateMessageReturnsFormattedMessageWithAvailableProducts() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(new ProductInventory(
            pepsi(), 10, 10, 60),
        new ProductInventory(coke(), 10, 10, 61)));

    IdleState idleState = new IdleState(vendingMachine);
    String message = idleState.getStateMessage();

    assertTrue(message.contains("Vending machine is ready!"));
    assertTrue(message.contains(pepsi().name()));
    assertTrue(message.contains(coke().name()));
  }

  @Test
  void getAvailableOptionsReturnsCorrectOptions() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getAvailableCoins()).thenReturn(List.of("PENNY, NICKEL, DIME, QUARTER"));
    IdleState idleState = new IdleState(vendingMachine);

    List<String> options = idleState.getAvailableOptions();

    assertEquals(2, options.size());
    assertTrue(
        options.contains("Insert coins! Accepted coins are: [PENNY, NICKEL, DIME, QUARTER]"));
    assertTrue(options.contains("Type 'card' to pay by credit card"));
  }

  @Test
  void inputActionTransitionsToCoinInsertedStateWhenCoinIsInserted() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    IdleState idleState = new IdleState(vendingMachine);

    idleState.inputAction("QUARTER");

    verify(vendingMachine).insertCoin("QUARTER");
    verify(vendingMachine).setState(any(CoinInsertedState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidInput() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    doThrow(new IllegalArgumentException("Invalid coin")).when(vendingMachine).insertCoin(anyString());

    IdleState idleState = new IdleState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> idleState.inputAction("INVALID"));
  }
}
