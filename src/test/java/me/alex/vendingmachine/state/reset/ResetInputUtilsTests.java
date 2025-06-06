package me.alex.vendingmachine.state.reset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import me.alex.vendingmachine.domain.state.reset.ResetInputUtils;
import org.junit.jupiter.api.Test;

public class ResetInputUtilsTests {

  @Test
  void getProductCodeParsesValidInputCorrectly() {
    int productCode = ResetInputUtils.getProductCode("123=10");
    assertEquals(123, productCode);
  }

  @Test
  void getProductCodeThrowsExceptionForInvalidFormat() {
    assertThrows(IllegalArgumentException.class,
        () -> ResetInputUtils.getProductCode("invalidInput"));
  }

  @Test
  void getProductQuantityParsesValidInputCorrectly() {
    int quantity = ResetInputUtils.getProductQuantity("123=10");
    assertEquals(10, quantity);
  }

  @Test
  void getProductQuantityThrowsExceptionForMissingQuantity() {
    assertThrows(IllegalArgumentException.class, () -> ResetInputUtils.getProductQuantity("123="));
  }

  @Test
  void getProductQuantityThrowsExceptionForInvalidQuantityFormat() {
    assertThrows(IllegalArgumentException.class,
        () -> ResetInputUtils.getProductQuantity("123=abc"));
  }

  @Test
  void getNewPriceParsesValidInputCorrectly() {
    BigDecimal price = ResetInputUtils.getNewPrice("123=19.99");
    assertEquals(new BigDecimal("19.99"), price);
  }

  @Test
  void getNewPriceThrowsExceptionForMissingPrice() {
    assertThrows(IllegalArgumentException.class, () -> ResetInputUtils.getNewPrice("123="));
  }

  @Test
  void getNewPriceThrowsExceptionForInvalidPriceFormat() {
    assertThrows(IllegalArgumentException.class,
        () -> ResetInputUtils.getNewPrice("123=invalidPrice"));
  }
}
