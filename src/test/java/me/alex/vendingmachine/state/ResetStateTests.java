package me.alex.vendingmachine.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.VendingMachineType;
import me.alex.vendingmachine.domain.state.ChangeProductPriceState;
import me.alex.vendingmachine.domain.state.ChangeProductQuantityState;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.ResetState;
import me.alex.vendingmachine.domain.state.UpdateChangeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResetStateTests {

  VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void getStateMessageReturnsCorrectMessage() {
    ResetState resetState = new ResetState(vendingMachine);
    assertEquals("You are in the reset mode", resetState.getStateMessage());
  }

  @Test
  void getAvailableOptionsReturnsAllResetOptions() {
    ResetState resetState = new ResetState(vendingMachine);
    List<String> options = resetState.getAvailableOptions();
    assertTrue(options.contains("Type 'default' to reset the vending machine to factory settings"));
    assertTrue(options.contains("Type 'price' to change product price"));
    assertTrue(options.contains("Type 'qty' to change product quantity"));
    assertTrue(options.contains("Type 'coin' to add or remove coins for change"));
    assertTrue(options.contains("Type 'exit' to exit the reset mode"));
  }

  @Test
  void stateActionResetsCurrentCreditToZero() {
    ResetState resetState = new ResetState(vendingMachine);
    vendingMachine.setCurrentCredit(new BigDecimal("5.00"));
    resetState.stateAction();
    verify(vendingMachine).setCurrentCredit(BigDecimal.ZERO);
  }

  @Test
  void inputActionResetsToFactorySettingsOnDefaultCommand() {
    when(vendingMachine.getVendingMachineType()).thenReturn(VendingMachineType.BEVERAGE);

    ResetState resetState = new ResetState(vendingMachine);
    resetState.inputAction("default");

    verify(vendingMachine).reset(any(VendingMachine.class));
    verify(vendingMachine).setState(any(IdleState.class));
  }

  @Test
  void inputActionExitsResetModeOnExitCommand() {
    ResetState resetState = new ResetState(vendingMachine);
    resetState.inputAction("exit");
    verify(vendingMachine).setState(any(IdleState.class));
  }

  @Test
  void inputActionChangesToChangeProductPriceStateOnPriceCommand() {
    ResetState resetState = new ResetState(vendingMachine);
    resetState.inputAction("price");
    verify(vendingMachine).setState(any(ChangeProductPriceState.class));
  }

  @Test
  void inputActionChangesToChangeProductQuantityStateOnQtyCommand() {
    ResetState resetState = new ResetState(vendingMachine);
    resetState.inputAction("qty");
    verify(vendingMachine).setState(any(ChangeProductQuantityState.class));
  }

  @Test
  void inputActionChangesToUpdateChangeStateOnCoinCommand() {
    ResetState resetState = new ResetState(vendingMachine);
    resetState.inputAction("coin");
    verify(vendingMachine).setState(any(UpdateChangeState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidCommand() {
    ResetState resetState = new ResetState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> resetState.inputAction("invalid"));
  }
}
