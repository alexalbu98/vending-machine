package me.alex.vendingmachine.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.CoinInsertedState;
import me.alex.vendingmachine.domain.state.DispensingState;
import me.alex.vendingmachine.domain.state.IdleState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DispensingStateTests {
  private VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void stateActionDispensesProductAndTransitionsToIdleStateWhenCreditIsZero() {
    when(vendingMachine.getCurrentCredit()).thenReturn(BigDecimal.ZERO);
    DispensingState state = new DispensingState(vendingMachine, 1);

    String result = state.stateAction();

    verify(vendingMachine).dispenseProduct(1);
    verify(vendingMachine).payProduct(1);
    verify(vendingMachine).setState(any(IdleState.class));
    assertEquals("Dispensing selected product... Enjoy!", result);
  }

  @Test
  void stateActionDispensesProductAndTransitionsToCoinInsertedStateWhenCreditIsNonZero() {
    when(vendingMachine.getCurrentCredit()).thenReturn(new BigDecimal("1.00"));
    DispensingState state = new DispensingState(vendingMachine, 1);

    String result = state.stateAction();

    verify(vendingMachine).dispenseProduct(1);
    verify(vendingMachine).payProduct(1);
    verify(vendingMachine).setState(any(CoinInsertedState.class));
    assertTrue(result.contains("Dispensing selected product... Enjoy!"));
    assertTrue(result.contains("not enough change"));
  }
}
