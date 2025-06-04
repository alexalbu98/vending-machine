package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class AddCoinState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String EMPTY_COMMAND = "empty";

  @Override
  public String getStateMessage() {
    return "Adding coins...\n" + formatCoinsAsString();
  }

  private String formatCoinsAsString() {
    return "Available coins: " + vendingMachine.getCoins().keySet().stream()
        .map(coin -> coin.value() + " (" + vendingMachine.getCoins().get(coin) + ")")
        .reduce((a, b) -> a + ", " + b)
        .orElse("There are no coins.");
  }

  @Override
  public List<String> getAvailableOptions() {
    return List.of("Type <coin value>+<quantity to add> to add coins:",
        "Type <coin value>-<quantity to remove> to remove coins:",
        "Type 'empty' to remove all coins");
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(EMPTY_COMMAND)) {
      vendingMachine.emptyAllCoins();
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    if (isAddition(input)) {
      vendingMachine.insertCoin(getCoinValue(input, "\\+"), getCoinQuantity(input, "\\+"));
    } else {
      vendingMachine.removeCoin(getCoinValue(input, "\\-"), getCoinQuantity(input, "\\-"));
    }
    vendingMachine.setState(new ResetState(vendingMachine));
  }

  private boolean isAddition(String input) {
    return input.split("\\+").length == 2;
  }

  private String getCoinValue(String input, String delimiter) {
    return input.split(delimiter)[0].trim();
  }

  private int getCoinQuantity(String input, String delimiter) {
    return Integer.parseInt(input.split(delimiter)[1].trim());
  }
}
