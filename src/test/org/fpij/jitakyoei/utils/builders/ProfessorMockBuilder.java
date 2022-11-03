package org.fpij.jitakyoei.utils.builders;

import java.util.List;

import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.utils.mocks.EntidadeMock;
import org.fpij.jitakyoei.utils.mocks.ProfessorMock;

public class ProfessorMockBuilder {
    private Professor professorInstance;

    public ProfessorMockBuilder() {
        professorInstance = new ProfessorMock().GetProfessorMock();
    }

    public ProfessorMockBuilder WithNome(String nome) {
        professorInstance.getFiliado().setNome(nome);
        return this;
    }

    public ProfessorMockBuilder WithRegistroCbj(String registroCbj) {
        professorInstance.getFiliado().setRegistroCbj(registroCbj);
        return this;
    }

    public ProfessorMockBuilder WithCpf(String cpf) {
        professorInstance.getFiliado().setRegistroCbj(cpf);
        return this;
    }

    public ProfessorMockBuilder WithEntidades(List<Entidade> entidades) {
        professorInstance.setEntidades(entidades);
        return this;
    }

    public ProfessorMockBuilder WithEntidadesMock(int entidadesAmount) {
        professorInstance.setEntidades(new EntidadeMock().GetEntidadesMock(entidadesAmount));
        return this;
    }

    public Professor Build() {
        return professorInstance;
    }
}
