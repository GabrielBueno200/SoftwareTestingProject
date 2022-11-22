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

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.utils.builders.AlunoMockBuilder;
import org.fpij.jitakyoei.view.forms.AlunoForm;
import org.fpij.jitakyoei.view.gui.AlunoAtualizarPanel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

public class AlunoAtualizarViewTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_AlunoComNomeVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String nomeNullOrEmpty) {
        Aluno aluno = new AlunoMockBuilder()
                .WithNome(nomeNullOrEmpty)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'nome' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "Gabriel 123", "@Henrique" })
    public void Atualizar_AlunoComNomeComCaracteresNaoAlfabeticos_ExibirAlertaDeNomeInvalido(String invalidNome) {
        Aluno aluno = new AlunoMockBuilder()
                .WithNome(invalidNome)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'nome' deve conter apenas letras");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_AlunoComRegistroCbjVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String registroCbjNullOrEmpty) {
        Aluno aluno = new AlunoMockBuilder()
                .WithRegistroCbj(registroCbjNullOrEmpty)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'Registro Cbj' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "123abc", "cdf@#" })
    public void Atualizar_AlunoComRegistroCbjComCaracteresNaoNumericos_ExibirAlertaDeRegistroCbjInvalido(
            String invalidRegistroCbj) {
        Aluno aluno = new AlunoMockBuilder()
                .WithRegistroCbj(invalidRegistroCbj)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'Registro Cbj' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_AlunoComCpfVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String cpfNullOrEmpty) {
        Aluno aluno = new AlunoMockBuilder()
                .WithCpf(cpfNullOrEmpty)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'CPF' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "698.568.978-3e", "@#1.234.567-#@" })
    public void Atualizar_AlunoComCpfComCaracteresNaoNumericos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
        Aluno aluno = new AlunoMockBuilder()
                .WithCpf(invalidCpf)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'CPF' deve conter apenas números");
    }

    @ParameterizedTest
    @ValueSource(strings = { "111.1141.111-23", "123.456.7899-55", "123.456.789-1" })
    public void Atualizar_AlunoComCpfMaiorOuMenorQue11Digitos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
        Aluno aluno = new AlunoMockBuilder()
                .WithCpf(invalidCpf)
                .Build();

        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'CPF' deve conter 11 dígitos!");
    }

    private void TestarAlunoComCampoVazioOuInvalido(Aluno aluno, String expectedErrorMessage) {
        // Arrange
        MainAppView mainAppViewMock = mock(MainAppView.class);

        AppFacade facadeMock = mock(AppFacade.class);
        doNothing().when(facadeMock).createAluno(any(Aluno.class));

        try (MockedConstruction<AlunoAtualizarPanel> panelMockScope = mockConstruction(
                AlunoAtualizarPanel.class,
                (alunoAtualizarPanelMock, context) -> AddButtonsMock(alunoAtualizarPanelMock))) {
            try (MockedConstruction<AlunoForm> formMockScope = mockConstruction(
                    AlunoForm.class,
                    (alunoFormMock, context) -> when(alunoFormMock.getAluno()).thenReturn(aluno))) {

                AlunoAtualizarView sut = new AlunoAtualizarView(mainAppViewMock, mock(Aluno.class));
                sut.registerFacade(facadeMock);

                JButton cadastrarButton = ((AlunoAtualizarPanel) sut.getGui()).getAtualizar();

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

    private void AddButtonsMock(AlunoAtualizarPanel atualizarAlunoPanelMock) {
        // Mock botão de atualizar
        JButton atualizarButtonSpy = spy(new JButton());
        doCallRealMethod().when(atualizarButtonSpy)
                .addActionListener(any(AlunoAtualizarView.AtualizarActionHandler.class));
        when(atualizarAlunoPanelMock.getAtualizar()).thenReturn(atualizarButtonSpy);

        // Mock botão de cancelar
        JButton cancelarButtonSpy = spy(new JButton());
        doNothing().when(cancelarButtonSpy).addActionListener(any(AlunoAtualizarView.CancelarActionHandler.class));
        when(atualizarAlunoPanelMock.getCancelar()).thenReturn(cancelarButtonSpy);

        // mock setVisible
        doNothing().when(atualizarAlunoPanelMock).setVisible(anyBoolean());
    }
}
