package br.com.luciano.payment.domain.exceptions;

public class BusinessException extends RuntimeException {

    private static final String MESSAGE = "Existe erro na requisição";

    public BusinessException() {
        this(MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }
}
