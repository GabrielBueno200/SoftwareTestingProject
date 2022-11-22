package org.fpij.jitakyoei.utils.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Professor;

import com.github.javafaker.Faker;

public class ProfessorMock {
    Faker faker;

    public ProfessorMock() {
        faker = new Faker(new Locale("pt-BR"));
    }

    public Professor GetProfessorMock() {
        Professor professor = new Professor();

        professor.setFiliado(new FiliadoMock().GetFiliadoMock());
        professor.setEntidades(new EntidadeMock().GetEntidadesMock(faker.number().numberBetween(1, 5)));

        return professor;
    }

    public List<Professor> GetProfessoresMock(int professoresAmount) {
        ArrayList<Professor> professores = new ArrayList<>();

        for (int i = 0; i < professoresAmount; i++)
            professores.add(GetProfessorMock());

        return professores;
    }
}
