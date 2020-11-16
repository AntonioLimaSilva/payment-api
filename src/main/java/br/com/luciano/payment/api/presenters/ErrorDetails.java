package br.com.luciano.payment.api.presenters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "ErrorDetails", description = "Informação detalhada dos erros ocorridos.")
@JsonPropertyOrder({ "uniqueId, informationCode, message, field, args" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDetails implements Serializable {

	private static final long serialVersionUID = -1745528769599021074L;

	@ApiModelProperty(position = 1, example = "10cb71fc-a126-4708-bb74-34dcd6ae47d5")
	private String uniqueId;

	@ApiModelProperty(position = 2, example = "Pattern")
	private String informationCode;

	@ApiModelProperty(position = 3, example = "Parametro de serviço não é válido")
	private String message;

	@ApiModelProperty(position = 5, example = "args")
	private String [] args;

	public ErrorDetails() { }

	public ErrorDetails(String uniqueId, String informationCode, String message, String[] args) {
		this.uniqueId = uniqueId;
		this.informationCode = informationCode;
		this.message = message;
		this.args = args;
	}
}