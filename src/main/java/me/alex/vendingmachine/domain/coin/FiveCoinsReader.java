package me.alex.vendingmachine.domain.coin;

public class FiveCoinsReader implements CoinReader {

  @Override
  public Coin readCoin(String value) {
    return CoinFactory.valueOf(value);
  }

}
