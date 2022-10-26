package org.fpij.jitakyoei.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Component;
import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.mocks.EntidadeEmptyTableMock;
import org.fpij.jitakyoei.mocks.EntidadeMock;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.view.gui.EntidadeBuscarPanel;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockedConstruction.MockInitializer;

import com.github.javafaker.Faker;

public class EntidadeBuscarViewTest {
    private static EntidadeEmptyTableMock entidadeTableMock;
    private static Faker faker;

    @BeforeAll
    public static void setUp() {
        entidadeTableMock = new EntidadeEmptyTableMock();
        faker = new Faker(new Locale("pt-BR"));
    }

    @Test
    public void Buscar_AoPesquisarEntidadeInexistente_ExibirAlertaEntidadeNaoEncontrada() {
        // Arrange
        EntidadeBuscarView sut = new EntidadeBuscarView();

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchEntidade(any(Entidade.class))).thenReturn(new ArrayList<Entidade>());
        sut.registerFacade(facadeMock);

        JButton searchButton = ((EntidadeBuscarPanel) sut.getGui()).getBtnBuscar();

        try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
            // Act
            searchButton.doClick();

            // Assert
            optionPaneMock.verify(
                    () -> JOptionPane.showMessageDialog(
                            any(Component.class),
                            eq("Não foram encontradas entidades com os dados fornecidos!"),
                            anyString(),
                            eq(JOptionPane.ERROR_MESSAGE)),
                    times(1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5 })
    public void Buscar_AposBuscarEntidadesValidas_TabelaDeveConterAsEntidades(int entidadesAmount) {
        // Arrange
        List<Entidade> entidadesSearchMock = new EntidadeMock().getEntidadesMock(entidadesAmount);

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchEntidade(any(Entidade.class))).thenReturn(entidadesSearchMock);

        JTextField textFieldNomeSpy = spy(new JTextField(faker.name().fullName()));

        try (MockedConstruction<EntidadeBuscarPanel> mocked = mockConstruction(
                EntidadeBuscarPanel.class,
                GetEntidadeBuscarPanelMock(textFieldNomeSpy))) {

            EntidadeBuscarView sut = new EntidadeBuscarView();
            sut.registerFacade(facadeMock);
            JButton searchButton = ((EntidadeBuscarPanel) sut.getGui()).getBtnBuscar();

            // Act
            searchButton.doClick();

            // Assert
            assertThat(entidadeTableMock.getRowCount()).isEqualTo(entidadesAmount);

            for (int i = 0; i < entidadeTableMock.getRowCount(); i++) {
                String nomeColumnValue = entidadeTableMock.getValueAt(i, 0).toString();
                String cnpjColumnValue = entidadeTableMock.getValueAt(i, 1).toString();
                String telefone1ColumnValue = entidadeTableMock.getValueAt(i, 2).toString();
                String telefone2ColumnValue = entidadeTableMock.getValueAt(i, 3).toString();

                assertThat(nomeColumnValue).isEqualTo(entidadesSearchMock.get(i).getNome());
                assertThat(cnpjColumnValue).isEqualTo(entidadesSearchMock.get(i).getCnpj());
                assertThat(telefone1ColumnValue).isEqualTo(entidadesSearchMock.get(i).getTelefone1());
                assertThat(telefone2ColumnValue).isEqualTo(entidadesSearchMock.get(i).getTelefone2());
            }
        }
    }

    private MockInitializer<EntidadeBuscarPanel> GetEntidadeBuscarPanelMock(JTextField textFieldNomeSpy)
    {
        return (entidadePanelMock, context) -> 
        {
            // Mock campos de busca
            when(entidadePanelMock.getNome()).thenReturn(textFieldNomeSpy);

            // Mock botão de busca
            JButton buttonSpy = spy(new JButton());
            doCallRealMethod().when(buttonSpy).addActionListener(any(EntidadeBuscarView.BuscarActionHandler.class));
            when(entidadePanelMock.getBtnBuscar()).thenReturn(buttonSpy);

            // Mock tabela de entidades
            when(entidadePanelMock.getTblEntidades()).thenReturn(entidadeTableMock);
        };
    }
}
