package org.fpij.jitakyoei.model.beans;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FiliadoTest {    
    @ParameterizedTest
    @ValueSource(strings = {"12345678910","12@45678910", "55824809923", "1234567891011"})
    public void CpfDeveConterApenas11CaracteresNumericos(String cpf) {
        // Arrange
        Filiado sut = new Filiado();
    
        // Act
        sut.setCpf(cpf);

        // Assert
        assertThat(sut.getCpf()).containsOnlyDigits().hasSize(11);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"gabriel", "Gabriel", "Gabriel Bueno", "hENRIQUE", "Henrique Vital", "Henrique vital"})
    public void NomeDeveComecarComLetraMaisculaEConterApenasLetrasEEspacos(String nome) {
        // Arrange
        Filiado sut = new Filiado();
    
        // Act
        sut.setNome(nome);

        // Assert
        assertThat(sut.getNome()).matches("([A-Z][a-z]+\\s*)+");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234","12@45"})
    public void RegistroCbjDeveConterApenasNumeros(String registroCbj) {
        // Arrange
        Filiado sut = new Filiado();
    
        // Act
        sut.setRegistroCbj(registroCbj);

        // Assert
        assertThat(sut.getRegistroCbj()).containsOnlyDigits();
    }
}
