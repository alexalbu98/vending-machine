package me.alex.vendingmachine.state;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.coin.Coin;
import me.alex.vendingmachine.domain.state.reset.ResetState;
import me.alex.vendingmachine.domain.state.reset.UpdateChangeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpdateChangeStateTests {

  private VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void getStateMessageDisplaysAvailableCoinsCorrectly() {
    when(vendingMachine.getCoins()).thenReturn(Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 10,
        new Coin("DIME", new BigDecimal("0.10")), 5
    ));
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    String message = state.getStateMessage();
    assertTrue(message.contains("0.25 (10)"));
    assertTrue(message.contains("0.10 (5)"));
  }

  @Test
  void getStateMessageDisplaysNoCoinsMessageWhenEmpty() {
    when(vendingMachine.getCoins()).thenReturn(Map.of());
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    String message = state.getStateMessage();
    assertTrue(message.contains("There are no coins."));
  }

  @Test
  void inputActionAddsCoinsSuccessfully() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    state.inputAction("0.25+5");
    verify(vendingMachine).insertCoin("0.25", 5);
    verify(vendingMachine).setState(any(ResetState.class));
  }

  @Test
  void inputActionRemovesCoinsSuccessfully() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    state.inputAction("0.10-3");
    verify(vendingMachine).removeCoin("0.10", 3);
    verify(vendingMachine).setState(any(ResetState.class));
  }

  @Test
  void inputActionEmptiesAllCoinsSuccessfully() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    state.inputAction("empty");
    verify(vendingMachine).emptyAllCoins();
    verify(vendingMachine).setState(any(ResetState.class));
  }

  @Test
  void inputActionExitsToResetStateSuccessfully() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    state.inputAction("exit");
    verify(vendingMachine).setState(any(ResetState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidCommand() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("invalid"));
  }

  @Test
  void inputActionThrowsExceptionForInvalidAdditionFormat() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("0.25+abc"));
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("+abc"));
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("0.25+"));
  }

  @Test
  void inputActionThrowsExceptionForInvalidSubtractionFormat() {
    UpdateChangeState state = new UpdateChangeState(vendingMachine);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("0.10-xyz"));
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("0.10-"));
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("-2"));
  }
}
