package org.fpij.jitakyoei.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class ProfessorMock {
    Faker faker;
    FakeValuesService fakeValuesService;

    public ProfessorMock() {
        faker = new Faker(new Locale("pt-BR"));

        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    public List<Professor> getProfessoresMock(int professoresAmount) {
        ArrayList<Professor> professores = new ArrayList<>();

        for (int i = 0; i < professoresAmount; i++) {
            Professor professor = new Professor();
            professor.setFiliado(new Filiado());
            professor.getFiliado().setNome(faker.name().fullName());
            professor.getFiliado().setId(faker.random().nextLong());
            professor.getFiliado().setRegistroCbj(faker.number().digits(3));

            professores.add(professor);
        }

        return professores;
    }
}
