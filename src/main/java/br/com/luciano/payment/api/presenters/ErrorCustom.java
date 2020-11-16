package br.com.luciano.payment.api.presenters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "Error", description = "Modelo de resposta do erro")
@JsonPropertyOrder({ "timestamp, status, error, message, path, errorDetails" })
public class ErrorCustom implements Serializable {

	private static final long serialVersionUID = 8118363273744924955L;

	@ApiModelProperty(position = 1, example = "2019-11-13 12:00:00")
	private String timestamp;

	@ApiModelProperty(position = 2, example = "500")
	private Integer status;

	@ApiModelProperty(position = 3, example = "Bad Request")
	private String error;

	@ApiModelProperty(position = 4, example = "Requisição possui campos inválidos")
	private String message;

	@ApiModelProperty(position = 5, example = "/slips")
	private String path;

	@ApiModelProperty(position = 6, example = "fba24a00-705c-4a14-83cb-4be00e625ca5")
	private String transactionId;

	@ApiModelProperty(position = 7)
	@JsonProperty(value = "errors")
	private List<ErrorDetails> errorDetails;

	public ErrorCustom() {}

	public ErrorCustom(String timestamp, Integer status, String error, String message, String path, String transactionId, List<ErrorDetails> errorDetails) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.transactionId = transactionId;
		this.errorDetails = errorDetails;
	}
}