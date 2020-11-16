package br.com.luciano.payment.api.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

    private final Integer status;
    private final String type;
    private final String title;
    private final String detail;
    private final List<Field> fields;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Field {
        private final String name;
        private final String message;
    }
}
