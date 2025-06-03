package me.alex.vendingmachine.domain.change;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import me.alex.vendingmachine.domain.coin.Coin;

public interface RefundPolicy {

  List<Change> refund(Map<Coin, Integer> coinQuantities, BigDecimal sum);
}
