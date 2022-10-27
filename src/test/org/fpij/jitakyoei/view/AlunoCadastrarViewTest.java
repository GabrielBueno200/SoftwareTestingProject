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

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Component;

import org.fpij.jitakyoei.builders.AlunoMockBuilder;
import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.view.forms.AlunoForm;
import org.fpij.jitakyoei.view.gui.AlunoCadastrarPanel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

public class AlunoCadastrarViewTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void Cadastrar_AlunoComNomeVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String nomeNullOrEmpty){
        Aluno aluno = new AlunoMockBuilder()
                        .WithNome(nomeNullOrEmpty)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'nome' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Gabriel 123", "@Henrique"})
    public void Cadastrar_AlunoComNomeComCaracteresInvalidos_ExibirAlertaDeNomeInvalido(String invalidNome){
        Aluno aluno = new AlunoMockBuilder()
                        .WithNome(invalidNome)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'nome' deve conter apenas letras");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void Cadastrar_AlunoComRegistroCbjVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String registroCbjNullOrEmpty){
        Aluno aluno = new AlunoMockBuilder()
                        .WithRegistroCbj(registroCbjNullOrEmpty)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'Registro Cbj' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123abc", "cdf@#"})
    public void Cadastrar_AlunoComRegistroCbjComCaracteresInvalidos_ExibirAlertaDeRegistroCbjInvalido(String invalidRegistroCbj){
        Aluno aluno = new AlunoMockBuilder()
                        .WithRegistroCbj(invalidRegistroCbj)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'Registro Cbj' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    public void Cadastrar_AlunoComCpfVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String cpfNullOrEmpty){
        Aluno aluno = new AlunoMockBuilder()
                        .WithCpf(cpfNullOrEmpty)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'CPF' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"abc568978de", "@#1234567#@"})
    public void Cadastrar_AlunoComCpfComCaracteresInvalidos_ExibirAlertaDeCpfInvalido(String invalidCpf){
        Aluno aluno = new AlunoMockBuilder()
                        .WithCpf(invalidCpf)
                        .Build();
        
        TestarAlunoComCampoVazioOuInvalido(aluno, "O campo 'CPF' deve conter apenas números");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1111111111123", "123456", "1234567891011"})
    public void Cadastrar_AlunoComCpfMaiorOuMenorQue11Digitos_ExibirAlertaDeCpfInvalido(String invalidCpf){
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

        try (MockedConstruction<AlunoCadastrarPanel> panelMockScope = mockConstruction(
            AlunoCadastrarPanel.class, 
            (cadastrarAlunoPanelMock, context) -> AddButtonsMock(cadastrarAlunoPanelMock))
        ){
            try (MockedConstruction<AlunoForm> formMockScope = mockConstruction(
                AlunoForm.class, 
                (alunoFormMock, context) -> when(alunoFormMock.getAluno()).thenReturn(aluno))
            ){

                AlunoCadastrarView sut = new AlunoCadastrarView(mainAppViewMock);
                sut.registerFacade(facadeMock);

                JButton cadastrarButton = ((AlunoCadastrarPanel)sut.getGui()).getCadastrar();
                
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

    private void AddButtonsMock(AlunoCadastrarPanel cadastrarAlunoPanelMock)
    {
        // Mock botão de cadastrar
        JButton cadastrarButtonSpy = spy(new JButton());
        doCallRealMethod().when(cadastrarButtonSpy).addActionListener(any(AlunoCadastrarView.CadastrarActionHandler.class));
        when(cadastrarAlunoPanelMock.getCadastrar()).thenReturn(cadastrarButtonSpy);
        
        // Mock botão de cancelar
        JButton cancelarButtonSpy = spy(new JButton());
        doNothing().when(cancelarButtonSpy).addActionListener(any(AlunoCadastrarView.CancelarActionHandler.class));
        when(cadastrarAlunoPanelMock.getCancelar()).thenReturn(cancelarButtonSpy);

        // mock setVisible
        doNothing().when(cadastrarAlunoPanelMock).setVisible(anyBoolean());
    }
}
