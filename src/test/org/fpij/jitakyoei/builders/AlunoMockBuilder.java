package org.fpij.jitakyoei.builders;

import org.fpij.jitakyoei.mocks.AlunoMock;
import org.fpij.jitakyoei.model.beans.Aluno;

public class AlunoMockBuilder {
    private Aluno alunoInstance = new AlunoMock().GetAlunoMock();;
    
    public AlunoMockBuilder WithNome(String nome){
        alunoInstance.getFiliado().setNome(nome);
        return this;
    }

    public AlunoMockBuilder WithRegistroCbj(String registroCbj) {
        alunoInstance.getFiliado().setRegistroCbj(registroCbj);
        return this;
    }

    public AlunoMockBuilder WithCpf(String cpf) {
        alunoInstance.getFiliado().setRegistroCbj(cpf);
        return this;
    }

    public Aluno Build(){
        return alunoInstance;
    }
}
