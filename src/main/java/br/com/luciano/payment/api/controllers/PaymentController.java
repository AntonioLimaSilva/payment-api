package br.com.luciano.payment.api.controllers;

import br.com.luciano.payment.api.presenters.ErrorCustom;
import br.com.luciano.payment.api.presenters.PaymentRequest;
import br.com.luciano.payment.api.presenters.PaymentResponse;
import br.com.luciano.payment.domain.services.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Payments")
@AllArgsConstructor
@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;

    @ApiResponses({
            @ApiResponse(code = 400, message = "BadRequest", response = ErrorCustom.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorCustom.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorCustom.class),
            @ApiResponse(code = 500, message = "InternalServerError", response = ErrorCustom.class)})
    @ApiOperation("Cadatra uma forma de pagamento")
    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.execute(request));
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "BadRequest", response = ErrorCustom.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorCustom.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorCustom.class),
            @ApiResponse(code = 500, message = "InternalServerError", response = ErrorCustom.class)})
    @ApiOperation("Consulta uma forma de pagamento")
    @GetMapping("/{number}")
    public ResponseEntity<PaymentResponse> verify(@PathVariable String number) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(paymentService.verify(number));
    }

}
