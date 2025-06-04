package me.alex.vendingmachine.domain.change;

import java.util.Map;
import me.alex.vendingmachine.domain.coin.Coin;

public interface IsChangeLowPolicy {

  boolean isChangeLow(Map<Coin, Integer> change);
}
