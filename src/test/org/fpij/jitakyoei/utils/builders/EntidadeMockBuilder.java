package org.fpij.jitakyoei.utils.builders;

import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.utils.mocks.EntidadeMock;

public class EntidadeMockBuilder {
    private Entidade entidadeInstance;

    public EntidadeMockBuilder() {
        entidadeInstance = new EntidadeMock().GetEntidadeMock();
    }

    public EntidadeMockBuilder WithNome(String nome) {
        entidadeInstance.setNome(nome);
        return this;
    }

    public EntidadeMockBuilder WithCnpj(String cnpj) {
        entidadeInstance.setCnpj(cnpj);
        return this;
    }

    public Entidade Build() {
        return entidadeInstance;
    }
}
