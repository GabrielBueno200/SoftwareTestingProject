package org.fpij.jitakyoei.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;

import com.github.javafaker.Faker;

public class ProfessorMock {
    Faker faker;

    public ProfessorMock() {
        faker = new Faker(new Locale("pt-BR"));
    }

    public Professor GetProfessorMock(){
        Professor professor = new Professor();
        
        Filiado filiado = new Filiado();
        filiado.setNome(faker.name().fullName());
        filiado.setId(faker.random().nextLong());
        filiado.setRegistroCbj(faker.number().digits(3));

        professor.setFiliado(filiado);
        
        return professor;
    }

    public List<Professor> GetProfessoresMock(int professoresAmount) {
        ArrayList<Professor> professores = new ArrayList<>();

        for (int i = 0; i < professoresAmount; i++) {
            
            professores.add(GetProfessorMock());
        }

        return professores;
    }
}
