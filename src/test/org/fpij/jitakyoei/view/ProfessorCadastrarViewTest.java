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
import org.fpij.jitakyoei.view.gui.ProfessorCadastrarPanel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

public class ProfessorCadastrarViewTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Cadastrar_ProfessorComNomeVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String nomeNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithNome(nomeNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'nome' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "Gabriel 123", "@Henrique" })
    public void Cadastrar_ProfessorComNomeComCaracteresNaoAlfabeticos_ExibirAlertaDeNomeInvalido(String invalidNome) {
        Professor professor = new ProfessorMockBuilder()
                .WithNome(invalidNome)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'nome' deve conter apenas letras");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Cadastrar_ProfessorComRegistroCbjVazioOuNulo_ExibirAlertaDeCampoObrigatorio(
            String registroCbjNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithRegistroCbj(registroCbjNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'Registro Cbj' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "123abc", "cdf@#" })
    public void Cadastrar_ProfessorComRegistroCbjComCaracteresNaoNumericos_ExibirAlertaDeRegistroCbjInvalido(
            String invalidRegistroCbj) {
        Professor professor = new ProfessorMockBuilder()
                .WithRegistroCbj(invalidRegistroCbj)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'Registro Cbj' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Cadastrar_ProfessorComCpfVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String cpfNullOrEmpty) {
        Professor professor = new ProfessorMockBuilder()
                .WithCpf(cpfNullOrEmpty)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'CPF' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "abc568978de", "@#1234567#@" })
    public void Cadastrar_ProfessorComCpfComCaracteresInvalidos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
        Professor professor = new ProfessorMockBuilder()
                .WithCpf(invalidCpf)
                .Build();

        TestarProfessorComCampoVazioOuInvalido(professor, "O campo 'CPF' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "1111111111123", "123456", "1234567891011" })
    public void Cadastrar_ProfessorComCpfMaiorOuMenorQue11Digitos_ExibirAlertaDeCpfInvalido(String invalidCpf) {
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

        try (MockedConstruction<ProfessorCadastrarPanel> panelMockScope = mockConstruction(
            ProfessorCadastrarPanel.class, 
            (cadastrarProfessorPanelMock, context) -> AddButtonsMock(cadastrarProfessorPanelMock))
        ){
            try (MockedConstruction<ProfessorForm> formMockScope = mockConstruction(
                ProfessorForm.class, 
                (professorFormMock, context) -> { 
                    when(professorFormMock.getProfessor()).thenReturn(professor);
                    when(professorFormMock.getEntidadesList()).thenReturn(professor.getEntidades());
                })
            ){

                ProfessorCadastrarView sut = new ProfessorCadastrarView(mainAppViewMock);
                sut.registerFacade(facadeMock);

                JButton cadastrarButton = ((ProfessorCadastrarPanel)sut.getGui()).getCadastrar();
                
                try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) 
                {
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

    private void AddButtonsMock(ProfessorCadastrarPanel cadastrarProfessorPanelMock)
    {
        // Mock botão de cadastrar
        JButton cadastrarButtonSpy = spy(new JButton());
        doCallRealMethod().when(cadastrarButtonSpy).addActionListener(any(ProfessorCadastrarView.CadastrarActionHandler.class));
        when(cadastrarProfessorPanelMock.getCadastrar()).thenReturn(cadastrarButtonSpy);
        
        // Mock botão de cancelar
        JButton cancelarButtonSpy = spy(new JButton());
        doNothing().when(cancelarButtonSpy).addActionListener(any(ProfessorCadastrarView.CancelarActionHandler.class));
        when(cadastrarProfessorPanelMock.getCancelar()).thenReturn(cancelarButtonSpy);

        // mock setVisible
        doNothing().when(cadastrarProfessorPanelMock).setVisible(anyBoolean());
    }
}
