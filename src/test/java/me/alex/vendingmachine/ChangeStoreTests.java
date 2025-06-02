package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import me.alex.vendingmachine.domain.ChangeStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeStoreTests {

  ChangeStore changeStore;

  @BeforeEach
  void initObjects() {
    changeStore = new ChangeStore();
  }

  @Test
  void addChangeIncreasesQuantityForExistingCoin() {
    changeStore.addChange("PENNY", 5);
    changeStore.addChange("PENNY", 3);
    assertEquals(8, changeStore.getChangeQuantity("PENNY"));
  }

  @Test
  void incrementChangeIncreasesQuantityByOne() {
    changeStore.addChange("DIME", 2);
    changeStore.incrementChange("DIME");
    assertEquals(3, changeStore.getChangeQuantity("DIME"));
  }

  @Test
  void decrementChangeDecreasesQuantityByOne() {
    changeStore.addChange("QUARTER", 3);
    changeStore.decrementChange("QUARTER");
    assertEquals(2, changeStore.getChangeQuantity("QUARTER"));
  }

  @Test
  void decrementChangeThrowsExceptionWhenQuantityIsZero() {
    changeStore.addChange("HALF_DOLLAR", 0);
    assertThrows(IllegalStateException.class, () -> changeStore.decrementChange("HALF_DOLLAR"));
  }

  @Test
  void decrementChangeThrowsExceptionForNonExistentCoin() {
    assertThrows(IllegalStateException.class, () -> changeStore.decrementChange("DOLLAR"));
  }

  @Test
  void getChangeQuantityThrowsExceptionForNullCoinName() {
    assertThrows(IllegalArgumentException.class, () -> changeStore.getChangeQuantity(null));
  }

  @Test
  void getChangeQuantityThrowsExceptionForNonExistentCoin() {
    assertThrows(IllegalArgumentException.class, () -> changeStore.getChangeQuantity("BITCOIN"));
  }
}
