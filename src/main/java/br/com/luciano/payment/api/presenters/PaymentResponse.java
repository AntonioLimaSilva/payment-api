package br.com.luciano.payment.api.presenters;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    @ApiModelProperty(example = "Cliente autorizado", position = 1)
    private final String message;
    @ApiModelProperty(position = 2)
    private final boolean isAuthorized;

}
