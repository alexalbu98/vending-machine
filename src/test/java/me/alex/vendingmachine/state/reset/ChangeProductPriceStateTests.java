package me.alex.vendingmachine.state.reset;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.product.Product;
import me.alex.vendingmachine.domain.product.ProductInventory;
import me.alex.vendingmachine.domain.state.reset.ChangeProductPriceState;
import me.alex.vendingmachine.domain.state.reset.ResetState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeProductPriceStateTests {

  private VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void getStateMessageDisplaysCorrectMessageWithProducts() {
    when(vendingMachine.getAvailableProducts()).thenReturn(List.of(
        new ProductInventory(new Product("Soda", new BigDecimal("1.50")), 10, 1, 60),
        new ProductInventory(new Product("Chips", new BigDecimal("2.00")), 10, 2, 61)
    ));
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    String message = state.getStateMessage();
    assertTrue(message.contains("Changing product price..."));
    assertTrue(message.contains("60 - Soda - price: 1.50, quantity: 1"));
    assertTrue(message.contains("61 - Chips - price: 2.00, quantity: 2"));
  }

  @Test
  void getAvailableOptionsReturnsCorrectOptions() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    List<String> options = state.getAvailableOptions();
    assertTrue(options.contains("Type <product code>=<price> to change its price:"));
    assertTrue(options.contains("Type 'exit' to return back"));
  }

  @Test
  void inputActionChangesProductPriceSuccessfully() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    state.inputAction("1=2.50");
    verify(vendingMachine).updateProductPrice(1, new BigDecimal("2.50"));
  }

  @Test
  void inputActionExitsToResetStateOnExitCommand() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    state.inputAction("exit");
    verify(vendingMachine).setState(any(ResetState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidInputFormat() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("invalid"));
  }

  @Test
  void inputActionThrowsExceptionForMissingPrice() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("1="));
  }

  @Test
  void inputActionThrowsExceptionForMissingProductCode() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("=1"));
  }

  @Test
  void inputActionThrowsExceptionForNonNumericPrice() {
    ChangeProductPriceState state = new ChangeProductPriceState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("1=abc"));
  }
}
