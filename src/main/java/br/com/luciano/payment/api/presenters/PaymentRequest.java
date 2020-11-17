package br.com.luciano.payment.api.presenters;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PaymentRequest {

    @ApiModelProperty(example = "Lucas Lima", position = 1)
    @NotBlank
    private String personName;
    @ApiModelProperty(example = "Cartão de Crédito", position = 2)
    @NotBlank
    private String type;
    @ApiModelProperty(example = "0988876509871234", position = 3)
    @NotBlank
    private String number;

}
