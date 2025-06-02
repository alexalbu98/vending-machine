package me.alex.vendingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.coin.Coin;
import me.alex.vendingmachine.domain.coin.FiveCoinsReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FiveCoinsReaderTests {

  @ParameterizedTest
  @ValueSource(strings = {"0.05", "0.10", "0.50", "1.00", "2.00"})
  void readCoinReturnsCorrectCoinForValidValue(String value) {
    FiveCoinsReader reader = new FiveCoinsReader();
    Coin coin = reader.readCoin(value);
    assertEquals(coin.value(), new BigDecimal(value));
  }

  @Test
  void readCoinThrowsExceptionForInvalidValue() {
    FiveCoinsReader reader = new FiveCoinsReader();
    assertThrows(IllegalArgumentException.class, () -> reader.readCoin("INVALID"));
  }

  @Test
  void readCoinHandlesNullValueGracefully() {
    FiveCoinsReader reader = new FiveCoinsReader();
    assertThrows(IllegalArgumentException.class, () -> reader.readCoin(null));
  }
}
