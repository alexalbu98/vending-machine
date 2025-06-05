package me.alex.vendingmachine.domain.payment;

import java.math.BigDecimal;

public interface PaymentProcessorClient {

  CardPaymentResult requestPayment(CreditCardDetails creditCardDetails, BigDecimal amount);
}
