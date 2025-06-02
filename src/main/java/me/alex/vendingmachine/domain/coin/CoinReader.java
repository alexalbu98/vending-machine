package me.alex.vendingmachine.domain.coin;

public interface CoinReader {

  Coin readCoin(String coinValue);
}
