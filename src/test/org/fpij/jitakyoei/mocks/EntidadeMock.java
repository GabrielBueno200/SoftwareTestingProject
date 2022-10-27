package org.fpij.jitakyoei.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Entidade;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class EntidadeMock {
    Faker faker;
    FakeValuesService fakeValuesService;

    private static final String regexCNPJ = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}-[0-9]";

    public EntidadeMock() {
        faker = new Faker(new Locale("pt-BR"));

        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    public List<Entidade> getEntidadesMock(int entidadesAmount) {
        ArrayList<Entidade> entidades = new ArrayList<>();

        for (int i = 0; i < entidadesAmount; i++) {
            Entidade entidade = new Entidade();

            entidade.setNome(faker.company().name());
            entidade.setCnpj(fakeValuesService.regexify(regexCNPJ));
            entidade.setTelefone1(faker.phoneNumber().phoneNumber());
            entidade.setTelefone2(faker.phoneNumber().cellPhone());

            entidades.add(entidade);
        }

        return entidades;
    }
}
