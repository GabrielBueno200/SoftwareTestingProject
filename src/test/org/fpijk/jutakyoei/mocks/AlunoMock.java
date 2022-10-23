package org.fpijk.jutakyoei.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class AlunoMock {
    Faker faker;
    FakeValuesService fakeValuesService;

    private static final String regexCNPJ = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}-[0-9]";

    public AlunoMock() {
        faker = new Faker(new Locale("pt-BR"));

        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    public List<Aluno> getAlunosMock(int quantidade) {
        ArrayList<Aluno> alunos = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            Professor professor = new Professor();
            professor.setFiliado(new Filiado());
            professor.getFiliado().setNome(faker.name().fullName());

            Entidade entidade = new Entidade();
            entidade.setNome(faker.company().name());
            entidade.setCnpj(fakeValuesService.regexify(regexCNPJ));

            Aluno aluno = new Aluno();
            aluno.setFiliado(new Filiado());
            aluno.getFiliado().setNome(faker.name().fullName());
            aluno.getFiliado().setId(faker.random().nextLong());
            aluno.getFiliado().setRegistroCbj(faker.number().digits(3));

            aluno.setProfessor(professor);
            aluno.setEntidade(entidade);

            alunos.add(aluno);
        }

        return alunos;
    }
}
