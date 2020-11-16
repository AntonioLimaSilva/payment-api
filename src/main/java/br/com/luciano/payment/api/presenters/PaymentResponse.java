package br.com.luciano.payment.api.presenters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private final String message;
    private final boolean isAuthorized;

}
