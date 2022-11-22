package org.fpij.jitakyoei.utils.builders;

import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.utils.mocks.AlunoMock;

public class AlunoMockBuilder {
    private Aluno alunoInstance;

    public AlunoMockBuilder() {
        alunoInstance = new AlunoMock().GetAlunoMock();
    }

    public AlunoMockBuilder WithNome(String nome) {
        alunoInstance.getFiliado().setNome(nome);
        return this;
    }

    public AlunoMockBuilder WithRegistroCbj(String registroCbj) {
        alunoInstance.getFiliado().setRegistroCbj(registroCbj);
        return this;
    }

    public AlunoMockBuilder WithCpf(String cpf) {
        alunoInstance.getFiliado().setCpf(cpf);
        return this;
    }

    public Aluno Build() {
        return alunoInstance;
    }
}
