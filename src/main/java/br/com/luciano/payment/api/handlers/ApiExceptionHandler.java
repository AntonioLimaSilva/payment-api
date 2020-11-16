package br.com.luciano.payment.api.handlers;

import br.com.luciano.payment.domain.exceptions.BusinessException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private static final String MSG_ERRO_SISTEMA = "Ocorreu um erro interno no sistema, tente novamente e se o problema persistir entre em contato com o " +
            "administrador do sistema";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleValidationRestauranteException(BusinessException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemType type = ProblemType.ERRO_SISTEMA;
        Problem problem = createProblemBuilder(status, type, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemType type = ProblemType.ERRO_SISTEMA;
        String detail = MSG_ERRO_SISTEMA;
        ex.printStackTrace();
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType type = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso '%s' que você tentou acessar, é inexistente", ex.getRequestURL());
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest webRequest) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, webRequest);
        } else if (rootCause instanceof UnrecognizedPropertyException) {
            return handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status, webRequest);
        }

        ProblemType type = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição esta invalído. Verifique erro de sintaxe";
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Problem.Field> fields = getFields(ex.getBindingResult());

        ProblemType type = ProblemType.DADOS_INVALIDOS;
        Problem problem = createProblemBuilder(status, type, MSG_ERRO_SISTEMA).fields(fields).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleNumberFormatException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /**
     * Todos os Handlers chamam esse handleExceptionInternal
     *
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .detail(ex.getMessage())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {

        String path = ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
        ProblemType type = ProblemType.FORMATO_INVALIDO;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s' que é do tipo inválido. " +
                "Corrija e informe um valor compatível com o tipo %s", path, ex.getValue(), ex.getTargetType().getSimpleName());
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, webRequest);
    }

    private ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        ProblemType type = ProblemType.PROPRIEDADE_DESCONHECIDA;

        String path = ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
        String detail = String.format("A propriedade '%s' não existe na classe %s", path, ex.getPathReference());
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, webRequest);
    }

    private ResponseEntity<Object> handleNumberFormatException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType type = ProblemType.PARAMETRO_INVALIDO;
        String detail = String.format("O parâmtro de URL '%s' recebeu o valor '%s', " +
                        "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        Problem problem = createProblemBuilder(status, type, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private List<Problem.Field> getFields(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, Locale.getDefault());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }
                    return Problem.Field.builder()
                            .message(message)
                            .name(name)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType type, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(type.getUrl())
                .title(type.getTitle())
                .detail(detail);
    }
}
