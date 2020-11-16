package br.com.luciano.payment.api.handlers;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("mensagem-incompreensivel", "Mensagem Incompreensível"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação da regra de Negócio"),
    FORMATO_INVALIDO("/formato-invalido", "Tipo dado inválido"),
    PROPRIEDADE_DESCONHECIDA("/propriedade-desconhecida", "A propriedade informada não existe"),
    PARAMETRO_INVALIDO("/parametro-invalido", "O paramêtro informado é inválido"),
    ERRO_SISTEMA("/erro-sistema", "Erro de sistema"),
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");

    private final String url;
    private final String title;

    ProblemType(String path, String title) {
        this.url = "http://baraodesobral.com.br".concat(path);
        this.title = title;
    }
}
