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
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.utils.builders.EntidadeMockBuilder;
import org.fpij.jitakyoei.view.forms.EntidadeForm;
import org.fpij.jitakyoei.view.gui.EntidadeAtualizarPanel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

public class EntidadeAtualizarViewTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_EntidadeComNomeVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String nomeNullOrEmpty) {
        Entidade entidade = new EntidadeMockBuilder()
                .WithNome(nomeNullOrEmpty)
                .Build();

        TestarEntidadeComCampoVazioOuInvalido(entidade, "O campo 'nome' é obrigatório");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void Atualizar_EntidadeComCnpjVazioOuNulo_ExibirAlertaDeCampoObrigatorio(String cnpjNullOrEmpty) {
        Entidade entidade = new EntidadeMockBuilder()
                .WithCnpj(cnpjNullOrEmpty)
                .Build();

        TestarEntidadeComCampoVazioOuInvalido(entidade, "O campo 'CNPJ' é obrigatório");
    }

    @ParameterizedTest
    @ValueSource(strings = { "55.555.555/55@-5", "55.555.555/55a-5" })
    public void Atualizar_EntidadeComCnpjComCaracteresNaoNumericos_ExibirAlertaDeCampoInvalido(String invalidCnjp) {
        Entidade entidade = new EntidadeMockBuilder()
                .WithCnpj(invalidCnjp)
                .Build();

        TestarEntidadeComCampoVazioOuInvalido(entidade, "O campo 'CPNJ' deve conter apenas números");
    }

    private void TestarEntidadeComCampoVazioOuInvalido(Entidade entidade, String expectedErrorMessage) {
        // Arrange
        MainAppView mainAppViewMock = mock(MainAppView.class);

        AppFacade facadeMock = mock(AppFacade.class);
        doNothing().when(facadeMock).createEntidade(any(Entidade.class));

        try (MockedConstruction<EntidadeAtualizarPanel> panelMockScope = mockConstruction(
                EntidadeAtualizarPanel.class,
                (atualizarEntidadePanelMock, context) -> AddButtonsMock(atualizarEntidadePanelMock))) {
            try (MockedConstruction<EntidadeForm> formMockScope = mockConstruction(
                    EntidadeForm.class,
                    (entidadeFormMock, context) -> when(entidadeFormMock.getEntidade()).thenReturn(entidade))) {

                EntidadeAtualizarView sut = new EntidadeAtualizarView(mainAppViewMock, mock(Entidade.class));
                sut.registerFacade(facadeMock);

                JButton cadastrarButton = ((EntidadeAtualizarPanel) sut.getGui()).getAtualizarEntidade();

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

    private void AddButtonsMock(EntidadeAtualizarPanel atualizarEntidadePanelMock) {
        // Mock botão de atualizar
        JButton atualizarButtonSpy = spy(new JButton());
        doCallRealMethod().when(atualizarButtonSpy)
                .addActionListener(any(EntidadeAtualizarView.AtualizarActionHandler.class));
        when(atualizarEntidadePanelMock.getAtualizarEntidade()).thenReturn(atualizarButtonSpy);

        // Mock botão de cancelar
        JButton cancelarButtonSpy = spy(new JButton());
        doNothing().when(cancelarButtonSpy).addActionListener(any(EntidadeAtualizarView.CancelarActionHandler.class));
        when(atualizarEntidadePanelMock.getCancelar()).thenReturn(cancelarButtonSpy);

        // mock setVisible
        doNothing().when(atualizarEntidadePanelMock).setVisible(anyBoolean());
    }
}
