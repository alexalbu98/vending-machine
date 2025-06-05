package me.alex.vendingmachine.state.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.payment.CardPaymentResult;
import me.alex.vendingmachine.domain.payment.CreditCardDetails;
import me.alex.vendingmachine.domain.state.DispensingState;
import me.alex.vendingmachine.domain.state.IdleState;
import me.alex.vendingmachine.domain.state.card.ProcessingPaymentState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProcessingPaymentStateTests {

  private VendingMachine vendingMachine;

  private final CreditCardDetails creditCardDetails = new CreditCardDetails("1234567890123456",
      "alex", "12/25",
      "123");

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void stateActionTransitionsToDispensingStateOnSuccessfulPayment() {
    when(vendingMachine.processCardPayment(creditCardDetails, "1"))
        .thenReturn(new CardPaymentResult(true, "Payment successful"));
    ProcessingPaymentState state = new ProcessingPaymentState(vendingMachine, creditCardDetails,
        "1");

    String result = state.stateAction();

    verify(vendingMachine).setState(any(DispensingState.class));
    assertEquals("Payment successful", result);
  }

  @Test
  void stateActionTransitionsToIdleStateOnFailedPayment() {
    when(vendingMachine.processCardPayment(creditCardDetails, "1"))
        .thenReturn(new CardPaymentResult(false, "Payment failed"));
    ProcessingPaymentState state = new ProcessingPaymentState(vendingMachine, creditCardDetails,
        "1");

    String result = state.stateAction();

    verify(vendingMachine).setState(any(IdleState.class));
    assertEquals("Payment failed", result);
  }
}
