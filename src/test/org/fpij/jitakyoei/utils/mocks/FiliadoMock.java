package org.fpij.jitakyoei.utils.mocks;

import java.util.Locale;

import org.fpij.jitakyoei.model.beans.Filiado;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class FiliadoMock {
    Faker faker;
    FakeValuesService fakeValuesService;

    private static final String regexCPF = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}";

    public FiliadoMock() {
        faker = new Faker(new Locale("pt-BR"));

        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    public Filiado GetFiliadoMock() {
        Filiado filiado = new Filiado();
        filiado.setNome(faker.name().fullName());
        filiado.setId(faker.random().nextLong());
        filiado.setCpf(fakeValuesService.regexify(regexCPF));
        filiado.setRegistroCbj(faker.number().digits(3));

        return filiado;
    }
}
