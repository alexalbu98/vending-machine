package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import me.alex.vendingmachine.domain.change.BigCoinsFirstRefundPolicy;
import me.alex.vendingmachine.domain.change.Change;
import me.alex.vendingmachine.domain.coin.Coin;
import org.junit.jupiter.api.Test;

public class BigCoinsFirstRefundPolicyTests {

  @Test
  void refundReturnsExactChangeWhenSufficientCoinsAvailable() {
    Map<Coin, Integer> coinQuantities = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 5,
        new Coin("DIME", new BigDecimal("0.10")), 5,
        new Coin("NICKEL", new BigDecimal("0.05")), 5
    );
    BigDecimal sum = new BigDecimal("0.65");
    BigCoinsFirstRefundPolicy policy = new BigCoinsFirstRefundPolicy();

    List<Change> changes = policy.refund(coinQuantities, sum);

    assertEquals(3, changes.size());
    assertEquals(2, changes.get(0).quantity());
    assertEquals(1, changes.get(1).quantity());
    assertEquals(1, changes.get(2).quantity());
  }

  @Test
  void refundThrowsExceptionWhenSumIsNegative() {
    Map<Coin, Integer> coinQuantities = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 4
    );
    BigDecimal sum = new BigDecimal("-0.25");
    BigCoinsFirstRefundPolicy policy = new BigCoinsFirstRefundPolicy();

    assertThrows(IllegalArgumentException.class, () -> policy.refund(coinQuantities, sum));
  }

  @Test
  void refundThrowsExceptionWhenExactChangeCannotBeReturned() {
    Map<Coin, Integer> coinQuantities = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 1,
        new Coin("DIME", new BigDecimal("0.10")), 1
    );
    BigDecimal sum = new BigDecimal("0.50");
    BigCoinsFirstRefundPolicy policy = new BigCoinsFirstRefundPolicy();

    var change = policy.refund(coinQuantities, sum);
    assertEquals(2, change.size());
    assertEquals("QUARTER", change.get(0).coin().name());
    assertEquals("DIME", change.get(1).coin().name());
  }

  @Test
  void refundReturnsEmptyListWhenSumIsZero() {
    Map<Coin, Integer> coinQuantities = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 4
    );
    BigDecimal sum = BigDecimal.ZERO;
    BigCoinsFirstRefundPolicy policy = new BigCoinsFirstRefundPolicy();

    List<Change> changes = policy.refund(coinQuantities, sum);
    assertTrue(changes.isEmpty());
  }

  @Test
  void refundHandlesCoinsWithZeroQuantity() {
    Map<Coin, Integer> coinQuantities = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 0,
        new Coin("DIME", new BigDecimal("0.10")), 3
    );
    BigDecimal sum = new BigDecimal("0.30");
    BigCoinsFirstRefundPolicy policy = new BigCoinsFirstRefundPolicy();

    List<Change> changes = policy.refund(coinQuantities, sum);

    assertEquals(1, changes.size());
    assertEquals(3, changes.get(0).quantity());
    assertEquals(new BigDecimal("0.30"), changes.stream()
        .map(change -> change.coin().value().multiply(new BigDecimal(change.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add));
  }
}
