package br.com.luciano.payment.domain.services;

import br.com.luciano.payment.api.presenters.PaymentRequest;
import br.com.luciano.payment.api.presenters.PaymentResponse;
import br.com.luciano.payment.domain.entities.PaymentEntity;
import br.com.luciano.payment.domain.exceptions.BusinessException;
import br.com.luciano.payment.domain.repositores.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentService {

    private static final String MESSAGE = "Já existe uma conta vinculada há esse cliente";

    private final PaymentRepository paymentRepository;

    public PaymentResponse execute(PaymentRequest request) {

        Optional<PaymentEntity> paymentOptional = paymentRepository.findByNumber(request.getNumber());

        paymentOptional.ifPresent( p -> { throw new BusinessException(MESSAGE); } );

        var newPayment = new PaymentEntity();
        BeanUtils.copyProperties(request, newPayment);

        paymentRepository.save(newPayment);

        return new PaymentResponse("Cadastrado com sucesso", true);
    }

    public PaymentResponse verify(String number) {
        Optional<PaymentEntity> paymentOptional = paymentRepository.findByNumber(number);

        return paymentOptional.map(p -> new PaymentResponse("Cliente tem crédito", true))
                .orElseGet(() -> new PaymentResponse("Cliente não tem crédito", false));
    }
}
