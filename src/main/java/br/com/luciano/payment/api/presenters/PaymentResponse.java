package br.com.luciano.payment.api.presenters;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    @ApiModelProperty(example = "Cliente autorizado")
    private final String message;
    private final boolean isAuthorized;

}
