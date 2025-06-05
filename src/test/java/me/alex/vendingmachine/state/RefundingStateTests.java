package me.alex.vendingmachine.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.change.Change;
import me.alex.vendingmachine.domain.coin.Coin;
import me.alex.vendingmachine.domain.state.CoinInsertedState;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.RefundingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RefundingStateTests {

  VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void stateActionTransitionsToIdleStateWhenCreditIsZero() {
    when(vendingMachine.getCurrentCredit()).thenReturn(BigDecimal.ZERO);
    when(vendingMachine.refund()).thenReturn(List.of());
    RefundingState state = new RefundingState(vendingMachine);

    String result = state.stateAction();

    verify(vendingMachine).setState(any(IdleState.class));
    assertEquals("Credit refunded successfully.", result.trim());
  }

  @Test
  void stateActionTransitionsToCoinInsertedStateWhenCreditIsNonZero() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getCurrentCredit()).thenReturn(new BigDecimal("1.00"));
    when(vendingMachine.refund()).thenReturn(List.of());
    RefundingState state = new RefundingState(vendingMachine);

    String result = state.stateAction();

    verify(vendingMachine).setState(any(CoinInsertedState.class));
    assertTrue(
        result.contains("Could not refund all credit, not enough change! Remaining credit: 1.00"));
  }

  @Test
  void stateActionFormatsRefundMessageCorrectlyForMultipleCoins() {
    VendingMachine vendingMachine = mock(VendingMachine.class);
    when(vendingMachine.getCurrentCredit()).thenReturn(BigDecimal.ZERO);
    when(vendingMachine.refund()).thenReturn(List.of(
        new Change(new Coin("QUARTER", new BigDecimal("0.25")), 4),
        new Change(new Coin("DIME", new BigDecimal("0.10")), 5)
    ));
    RefundingState state = new RefundingState(vendingMachine);

    String result = state.stateAction();

    assertTrue(result.contains("Refunded 4 coin(s) of value 0.25"));
    assertTrue(result.contains("Refunded 5 coin(s) of value 0.10"));
    assertTrue(result.contains("Credit refunded successfully."));
  }
}
