package org.fpij.jitakyoei.utils.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Aluno;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class AlunoMock {
    Faker faker;
    FakeValuesService fakeValuesService;

    public AlunoMock() {
        faker = new Faker(new Locale("pt-BR"));

        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    public Aluno GetAlunoMock() {
        Aluno aluno = new Aluno();

        aluno.setFiliado(new FiliadoMock().GetFiliadoMock());
        aluno.setProfessor(new ProfessorMock().GetProfessorMock());
        aluno.setEntidade(new EntidadeMock().GetEntidadeMock());

        return aluno;
    }

    public List<Aluno> GetAlunosMock(int alunosAmount) {
        ArrayList<Aluno> alunos = new ArrayList<>();

        for (int i = 0; i < alunosAmount; i++)
            alunos.add(GetAlunoMock());

        return alunos;
    }
}
