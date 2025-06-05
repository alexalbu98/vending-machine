package me.alex.vendingmachine.domain.payment;

import java.math.BigDecimal;

public class OfflinePaymentProcessorClient implements PaymentProcessorClient {

  @Override
  public CardPaymentResult requestPayment(CreditCardDetails creditCardDetails, BigDecimal amount) {
    sleep();
    return new CardPaymentResult(true, "Payment with amount " + amount + " processed successfully");
  }

  void sleep() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
