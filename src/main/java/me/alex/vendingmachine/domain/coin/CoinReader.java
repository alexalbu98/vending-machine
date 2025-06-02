package me.alex.vendingmachine.domain.coin;

import java.util.List;

public interface CoinReader {

  Coin readCoin(String coinValue);
  List<String> availableCoins();
}
