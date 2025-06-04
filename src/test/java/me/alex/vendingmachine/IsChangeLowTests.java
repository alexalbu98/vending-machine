package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Map;
import me.alex.vendingmachine.domain.change.IsChangeLowByCoinNumberAndQuantity;
import me.alex.vendingmachine.domain.coin.Coin;
import org.junit.jupiter.api.Test;

public class IsChangeLowTests {

  @Test
  void isChangeLowReturnsTrueWhenCoinNumberIsBelowThreshold() {
    Map<Coin, Integer> change = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 10,
        new Coin("DIME", new BigDecimal("0.10")), 5,
        new Coin("NICKEL", new BigDecimal("0.05")), 3
    );
    IsChangeLowByCoinNumberAndQuantity policy = new IsChangeLowByCoinNumberAndQuantity();

    assertTrue(policy.isChangeLow(change));
  }

  @Test
  void isChangeLowReturnsFalseWhenAllCoinQuantitiesAreAboveThreshold() {
    Map<Coin, Integer> change = Map.of(
        new Coin("QUARTER", new BigDecimal("0.25")), 6,
        new Coin("DIME", new BigDecimal("0.10")), 7,
        new Coin("NICKEL", new BigDecimal("0.05")), 8,
        new Coin("PENNY", new BigDecimal("0.01")), 10
    );
    IsChangeLowByCoinNumberAndQuantity policy = new IsChangeLowByCoinNumberAndQuantity();

    assertFalse(policy.isChangeLow(change));
  }

  @Test
  void isChangeLowReturnsTrueForEmptyChangeMap() {
    Map<Coin, Integer> change = Map.of();
    IsChangeLowByCoinNumberAndQuantity policy = new IsChangeLowByCoinNumberAndQuantity();

    assertTrue(policy.isChangeLow(change));
  }
}
