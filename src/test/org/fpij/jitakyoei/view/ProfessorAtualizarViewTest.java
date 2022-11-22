package org.fpij.jitakyoei.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.utils.builders.ProfessorMockBuilder;
import org.fpij.jitakyoei.view.forms.ProfessorForm;
import org.fpij.jitakyoei.view.gui.ProfessorAtualizarPanel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

public class ProfessorAtualizarViewTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_ProfessorComNomeVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String nomeNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithNome(nomeNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'nome' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "Gabriel 123", "@Henrique" })
    public void Atualizar_ProfessorComNomeComCaracteresNaoAlfabeticos_ExibirAlertaDeNomeInvalido(String invalidNome) {
        Professor professor = new ProfessorMockBuilder()
                .WithNome(invalidNome)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'nome' deve conter apenas letras");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_ProfessorComRegistroCbjVazioOuNulo_ExibirAlertaDeCampoObrigatorio(
            String registroCbjNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithRegistroCbj(registroCbjNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'Registro Cbj' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "123abc", "cdf@#" })
    public void Atualizar_ProfessorComRegistroCbjComCaracteresNaoNumericos_ExibirAlertaDeRegistroCbjInvalido(
            String invalidRegistroCbj) {
        Professor professor = new ProfessorMockBuilder()
                .WithRegistroCbj(invalidRegistroCbj)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'Registro Cbj' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_ProfessorComCpfVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String cpfNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithCpf(cpfNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'CPF' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "698.568.978-3e", "@#1.234.567-#@" })
    public void Atualizar_ProfessorComCpfComCaracteresInvalidos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
        Professor professor = new ProfessorMockBuilder()
                .WithCpf(invalidCpf)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'CPF' deve conter apenas números");
    }

    @ParameterizedTest
    @ValueSource(strings = { "111.1141.111-23", "123.456.7899-55", "123.456.789-1" })
    public void Atualizar_ProfessorComCpfMaiorOuMenorQue11Digitos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
        Professor professor = new ProfessorMockBuilder()
                .WithCpf(invalidCpf)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'CPF' deve conter 11 dígitos!");
    }

    private void TestarProfessorComCampoVazioOuInvalido(Professor professor, String expectedErrorMessage) {
        // Arrange
        MainAppView mainAppViewMock = mock(MainAppView.class);

        AppFacade facadeMock = mock(AppFacade.class);
        doNothing().when(facadeMock).createProfessor(any(Professor.class));
        doNothing().when(facadeMock).createProfessorEntidade(any(List.class));

        try (MockedConstruction<ProfessorAtualizarPanel> panelMockScope = mockConstruction(
                ProfessorAtualizarPanel.class,
                (professorAtualizarPanelMock, context) -> AddButtonsMock(professorAtualizarPanelMock))) {
            try (MockedConstruction<ProfessorForm> formMockScope = mockConstruction(
                    ProfessorForm.class,
                    (professorFormMock, context) -> {
                        when(professorFormMock.getProfessor()).thenReturn(professor);
                        when(professorFormMock.getEntidadesList()).thenReturn(professor.getEntidades());
                    })) {

                ProfessorAtualizarView sut = new ProfessorAtualizarView(mainAppViewMock, mock(Professor.class));
                sut.registerFacade(facadeMock);

                JButton cadastrarButton = ((ProfessorAtualizarPanel) sut.getGui()).getAtualizar();

                try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
                    // Act
                    cadastrarButton.doClick();

                    // Assert
                    optionPaneMock.verify(
                            () -> JOptionPane.showMessageDialog(
                                    any(Component.class),
                                    eq(expectedErrorMessage),
                                    anyString(),
                                    eq(JOptionPane.ERROR_MESSAGE)),
                            times(1));
                }
            }
        }
    }

    private void AddButtonsMock(ProfessorAtualizarPanel atualizarProfessorPanelMock) {
        // Mock botão de cadastrar
        JButton atualizarButtonSpy = spy(new JButton());
        doCallRealMethod().when(atualizarButtonSpy)
                .addActionListener(any(ProfessorAtualizarView.AtualizarActionHandler.class));
        when(atualizarProfessorPanelMock.getAtualizar()).thenReturn(atualizarButtonSpy);

        // Mock botão de cancelar
        JButton cancelarButtonSpy = spy(new JButton());
        doNothing().when(cancelarButtonSpy).addActionListener(any(ProfessorAtualizarView.CancelarActionHandler.class));
        when(atualizarProfessorPanelMock.getCancelar()).thenReturn(cancelarButtonSpy);

        // mock setVisible
        doNothing().when(atualizarProfessorPanelMock).setVisible(anyBoolean());
    }
}
