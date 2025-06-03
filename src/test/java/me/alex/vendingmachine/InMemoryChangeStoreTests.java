package me.alex.vendingmachine;

import static me.alex.vendingmachine.domain.coin.CoinFactory.coin;
import static me.alex.vendingmachine.domain.coin.CoinFactory.dime;
import static me.alex.vendingmachine.domain.coin.CoinFactory.halfCoin;
import static me.alex.vendingmachine.domain.coin.CoinFactory.nickel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import me.alex.vendingmachine.domain.change.BigCoinsFirstRefundPolicy;
import me.alex.vendingmachine.domain.change.Change;
import me.alex.vendingmachine.domain.change.ChangeStore;
import me.alex.vendingmachine.domain.change.InMemoryChangeStore;
import me.alex.vendingmachine.domain.coin.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryChangeStoreTests {

  ChangeStore changeStore;

  @BeforeEach
  void initObjects() {
    changeStore = new InMemoryChangeStore(new BigCoinsFirstRefundPolicy());
  }

  @Test
  void addChangeIncreasesQuantityForExistingCoin() {
    changeStore.addChange(nickel(), 5);
    changeStore.addChange(nickel(), 3);
    assertEquals(8, changeStore.getChangeQuantity(nickel()));
  }

  @Test
  void incrementChangeIncreasesQuantityByOne() {
    changeStore.addChange(dime(), 2);
    changeStore.incrementChange(dime());
    assertEquals(3, changeStore.getChangeQuantity(dime()));
  }

  @Test
  void decrementChangeDecreasesQuantityByOne() {
    changeStore.addChange(halfCoin(), 3);
    changeStore.decrementChange(halfCoin());
    assertEquals(2, changeStore.getChangeQuantity(halfCoin()));
  }

  @Test
  void decrementChangeThrowsExceptionWhenQuantityIsZero() {
    changeStore.addChange(coin(), 0);
    assertThrows(IllegalStateException.class, () -> changeStore.decrementChange(coin()));
  }

  @Test
  void decrementChangeThrowsExceptionForNonExistentCoin() {
    assertThrows(IllegalStateException.class,
        () -> changeStore.decrementChange(new Coin("test", new BigDecimal("1.00"))));
  }

  @Test
  void getChangeQuantityThrowsExceptionForNullCoinName() {
    assertThrows(IllegalArgumentException.class, () -> changeStore.getChangeQuantity(null));
  }

  @Test
  void getChangeQuantityThrowsExceptionForNonExistentCoin() {
    assertThrows(IllegalArgumentException.class,
        () -> changeStore.getChangeQuantity(new Coin("test", new BigDecimal("1.00"))));
  }

  @Test
  void refundDecreasesCoinQuantitiesCorrectly() {
    changeStore.addChange(dime(), 5);
    changeStore.addChange(nickel(), 3);
    List<Change> changes = changeStore.refund(new BigDecimal("0.25"));
    assertEquals(2, changes.size());
    assertEquals(3, changeStore.getChangeQuantity(dime()));
    assertEquals(2, changeStore.getChangeQuantity(nickel()));
  }
}
