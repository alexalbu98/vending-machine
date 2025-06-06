package me.alex.vendingmachine.domain.state.reset;

import java.math.BigDecimal;

public final class ResetInputUtils {

  private ResetInputUtils() {
  }

  public static int getProductCode(String input) {
    try {
      return Integer.parseInt(input.split("=")[0].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid product code format. Expected an integer for product code.");
    }
  }

  public static Integer getProductQuantity(String input) {
    var args = input.split("=");
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Invalid input format. Expected format: <product code>=<quantity>");
    }
    try {
      return Integer.parseInt(args[1].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid product quantity format. Expected a number for value.");
    }
  }

  public static BigDecimal getNewPrice(String input) {
    var args = input.split("=");
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Invalid input format. Expected format: <product code>=<price>");
    }
    try {
      return new BigDecimal(args[1].trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid product code format. Expected a number for value.");
    }
  }
}
