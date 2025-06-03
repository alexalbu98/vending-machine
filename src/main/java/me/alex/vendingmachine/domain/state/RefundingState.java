package me.alex.vendingmachine.domain.state;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;
import me.alex.vendingmachine.domain.change.Change;

@RequiredArgsConstructor
public class RefundingState implements VendingMachineState {

  private final VendingMachine vendingMachine;

  @Override
  public String getStateMessage() {
    return "";
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of();
  }

  @Override
  public String stateAction() {
    var change = vendingMachine.refund();
    if (vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO) == 0) {
      vendingMachine.setState(new IdleState(vendingMachine));
    } else {
      vendingMachine.setState(new CoinInsertedState(vendingMachine));
    }
    return formatRefundMessage(change);
  }

  private String formatRefundMessage(List<Change> change) {
    StringBuilder stringBuilder = new StringBuilder();
    change.forEach(c ->
        stringBuilder.append("Refunded ")
            .append(c.quantity())
            .append(" coins of value ")
            .append(c.coin().value()).append("\n")
    );
    if (vendingMachine.getCurrentCredit().compareTo(BigDecimal.ZERO) == 0) {
      stringBuilder.append("Credit refunded successfully.");
    } else {
      stringBuilder.append("Could not refund all credit, not enough change! Remaining credit: ")
          .append(vendingMachine.getCurrentCredit());
    }
    return stringBuilder.toString();
  }

  @Override
  public void inputAction(String input) {

  }
}
