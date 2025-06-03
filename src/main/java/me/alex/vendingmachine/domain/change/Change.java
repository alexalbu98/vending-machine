package me.alex.vendingmachine.domain.change;

import me.alex.vendingmachine.domain.coin.Coin;

public record Change(Coin coin, int quantity){
}
