package br.com.luciano.payment.api.presenters;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PaymentRequest {

    @NotBlank
    private String personName;
    @NotBlank
    private String type;
    @NotBlank
    private String number;

}
