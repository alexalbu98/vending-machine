package me.alex.vendingmachine.domain.state;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.alex.vendingmachine.domain.VendingMachine;

@RequiredArgsConstructor
public class UpdateChangeState implements VendingMachineState {

  private final VendingMachine vendingMachine;
  private final String EMPTY_COMMAND = "empty";
  private final String EXIT = "exit";

  @Override
  public String getStateMessage() {
    return "Updating coins...\n" + formatCoinsAsString();
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
        "Type 'empty' to remove all coins",
        "Type 'exit' to return all coins"
    );
  }

  @Override
  public String stateAction() {
    return "";
  }

  @Override
  public void inputAction(String input) {
    if (input.equals(EXIT)) {
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    if (input.equals(EMPTY_COMMAND)) {
      vendingMachine.emptyAllCoins();
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    if (isAddition(input)) {
      vendingMachine.insertCoin(getCoinValue(input, "\\+"), getCoinQuantity(input, "\\+"));
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    if (isSubstraction(input)) {
      vendingMachine.removeCoin(getCoinValue(input, "\\-"), getCoinQuantity(input, "\\-"));
      vendingMachine.setState(new ResetState(vendingMachine));
      return;
    }
    throw new IllegalArgumentException("Invalid input: " + input + ". Not a known command.");
  }

  private boolean isAddition(String input) {
    return input.split("\\+").length == 2;
  }

  private boolean isSubstraction(String input) {
    return input.split("-").length == 2;
  }

  private String getCoinValue(String input, String delimiter) {
    return input.split(delimiter)[0].trim();
  }

  private int getCoinQuantity(String input, String delimiter) {
    return Integer.parseInt(input.split(delimiter)[1].trim());
  }
}
