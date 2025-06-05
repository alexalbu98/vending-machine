package me.alex.vendingmachine.state.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.state.card.ProcessingPaymentState;
import me.alex.vendingmachine.domain.state.card.ReadCardDetailsState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReadCardDetailsStateTests {

  private VendingMachine vendingMachine;

  @BeforeEach
  void setupMocks() {
    vendingMachine = mock(VendingMachine.class);
  }

  @Test
  void getStateMessageDisplaysPromptForCardDetails() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", null, null, null,
        null);
    String message = state.getStateMessage();
    assertEquals("Please enter card details!", message);
  }

  @Test
  void getAvailableOptionsPromptsForCardNumberWhenMissing() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", null, null, null,
        null);
    List<String> options = state.getAvailableOptions();
    assertTrue(options.contains("Please enter card number:"));
  }

  @Test
  void getAvailableOptionsPromptsForExpiryDateWhenCardNumberIsProvided() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", "1234567890123456",
        null, null, null);
    List<String> options = state.getAvailableOptions();
    assertTrue(options.contains("Please enter card expiry date (MM/YY):"));
  }

  @Test
  void inputActionTransitionsToNextStateAfterValidCardNumber() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", null, null, null,
        null);
    state.inputAction("1234567890123456");
    verify(vendingMachine).setState(any(ReadCardDetailsState.class));
  }

  @Test
  void inputActionTransitionsToNextStateAfterValidExpiryDate() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", "1234567890123456",
        null, null, null);
    state.inputAction("12/25");
    verify(vendingMachine).setState(any(ReadCardDetailsState.class));
  }

  @Test
  void inputActionTransitionsToProcessingPaymentStateAfterValidCVV() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", "1234567890123456",
        "12/25", "John Doe", null);
    state.inputAction("123");
    verify(vendingMachine).setState(any(ProcessingPaymentState.class));
  }

  @Test
  void inputActionThrowsExceptionForInvalidCardNumberFormat() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", null, null, null,
        null);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("invalidCardNumber"));
  }

  @Test
  void inputActionThrowsExceptionForInvalidExpiryDateFormat() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", "1234567890123456",
        null, null, null);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("invalidDate"));
  }

  @Test
  void inputActionThrowsExceptionForInvalidCVVFormat() {
    ReadCardDetailsState state = new ReadCardDetailsState(vendingMachine, "1", "1234567890123456",
        "12/25", "John Doe", null);
    assertThrows(IllegalArgumentException.class, () -> state.inputAction("12"));
  }
}
