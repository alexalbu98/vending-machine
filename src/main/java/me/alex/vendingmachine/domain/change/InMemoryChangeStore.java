package me.alex.vendingmachine.domain.change;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import me.alex.vendingmachine.domain.coin.Coin;

public class InMemoryChangeStore implements ChangeStore {

  private final Map<Coin, Integer> coinMap = new TreeMap<>();
  private final RefundPolicy refundPolicy;
  private final IsChangeLowPolicy isChangeLowPolicy;

  public InMemoryChangeStore(RefundPolicy refundPolicy, IsChangeLowPolicy isChangeLowPolicy) {
    this.refundPolicy = refundPolicy;
    this.isChangeLowPolicy = isChangeLowPolicy;
  }

  public void addChange(Coin coin, int quantity) {
    coinMap.put(coin, coinMap.getOrDefault(coin, 0) + quantity);
  }

  public void incrementChange(Coin coin) {
    coinMap.put(coin, coinMap.getOrDefault(coin, 0) + 1);
  }

  public void decrementChange(Coin coin) {
    if (!coinMap.containsKey(coin) || coinMap.get(coin) == 0) {
      throw new IllegalStateException("Cannot decrement change for " + coin.name() + " below zero");
    }
    coinMap.put(coin, coinMap.get(coin) - 1);
  }

  public int getChangeQuantity(Coin coin) {
    if (coin == null || !coinMap.containsKey(coin)) {
      throw new IllegalArgumentException(
          "Coin name cannot be null and it must exist in the change storage");
    }
    return coinMap.getOrDefault(coin, 0);
  }

  @Override
  public List<Change> refund(BigDecimal sum) {
    var changeList = refundPolicy.refund(coinMap, sum);
    changeList.forEach(c -> {
      var newQuantity = coinMap.get(c.coin()) - c.quantity();
      coinMap.put(c.coin(), newQuantity);
    });
    return changeList;
  }

  @Override
  public boolean isLowOnChange() {
    return isChangeLowPolicy.isChangeLow(coinMap);
  }

}
