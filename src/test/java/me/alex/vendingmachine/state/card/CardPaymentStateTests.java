package me.alex.vendingmachine.state.card;

import static me.alex.vendingmachine.domain.product.ProductFactory.coke;
import static me.alex.vendingmachine.domain.product.ProductFactory.pepsi;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.card.CardPaymentState;
import me.alex.vendingmachine.domain.state.card.ReadCardDetailsState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardPaymentStateTests {

  private VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(
        new ProductInventory(pepsi(), 10, 10, 60),
        new ProductInventory(coke(), 10, 10, 61)));
  }

  @Test
  void inputActionTransitionsToIdleStateOnExitCommand() {
    CardPaymentState state = new CardPaymentState(vendingMachine);
    state.inputAction("exit");
    verify(vendingMachine).setState(any(IdleState.class));
  }

  @Test
  void inputActionTransitionsToReadCardDetailsStateForValidProductCode() {
    when(vendingMachine.productCodeExists("1")).thenReturn(true);
    doNothing().when(vendingMachine).verifyProductQuantity("1");
    CardPaymentState state = new CardPaymentState(vendingMachine);
    state.inputAction("1");
    verify(vendingMachine).setState(any(ReadCardDetailsState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidProductCode() {
    when(vendingMachine.productCodeExists("999")).thenReturn(false);
    CardPaymentState state = new CardPaymentState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("999"));
  }
}
