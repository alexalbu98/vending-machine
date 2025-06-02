package me.alex.vendingmachine.domain.coin;

import static me.alex.vendingmachine.domain.coin.CoinFactory.coin;
import static me.alex.vendingmachine.domain.coin.CoinFactory.dime;
import static me.alex.vendingmachine.domain.coin.CoinFactory.halfCoin;
import static me.alex.vendingmachine.domain.coin.CoinFactory.nickel;
import static me.alex.vendingmachine.domain.coin.CoinFactory.twoCoin;
import static me.alex.vendingmachine.domain.coin.CoinFactory.valueOf;

import java.util.List;

public class FiveCoinsReader implements CoinReader {

  @Override
  public Coin readCoin(String value) {
    return valueOf(value);
  }

  @Override
  public List<String> availableCoins() {
    return List.of(nickel().value().toString(),
        dime().value().toString(),
        halfCoin().value().toString(),
        coin().value().toString(),
        twoCoin().value().toString()
    );
  }

}
