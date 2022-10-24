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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.view.gui.BuscaCamposPanel;
import org.fpij.jitakyoei.view.gui.ProfessorBuscarPanel;
import org.fpijk.jutakyoei.mocks.ProfessorEmptyTableMock;
import org.fpijk.jutakyoei.mocks.ProfessorMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.MockedConstruction.MockInitializer;
import org.mockito.MockedStatic;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class ProfessorBuscarViewTest {
    private static JTable professorTableMock;
    private static Faker faker;
    private static FakeValuesService fakeValuesService;

    @BeforeAll
    public static void setUp() {
        professorTableMock = new ProfessorEmptyTableMock();
        faker = new Faker(new Locale("pt-BR"));
        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    @Test
    public void Buscar_InserirRegistroComLetras_ExibirAlertaDeRegistroInvalido() {
        // Arrange
        JTextField textFieldRegistroSpy = spy(new JTextField(fakeValuesService.regexify("[a-zA-Z]+")));
        JTextField textFieldNomeSpy = spy(new JTextField(""));

        try (MockedConstruction<ProfessorBuscarPanel> mocked = mockConstruction(
                ProfessorBuscarPanel.class,
                GetProfessorBuscarPanelMock(textFieldNomeSpy, textFieldRegistroSpy))) {

            ProfessorBuscarView sut = new ProfessorBuscarView();
            JButton searchButton = ((ProfessorBuscarPanel) sut.getGui()).getBuscar();

            try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
                // Act
                searchButton.doClick();

                // Assert
                optionPaneMock.verify(
                        () -> JOptionPane.showMessageDialog(
                                any(Component.class),
                                eq("Digite um número de registro válido!")),
                        times(1));
            }
        }
    }

    @Test
    public void Buscar_AoPesquisarProfessorInexistente_ExibirAlertaProfessorNaoEncontrado() {
        // Arrange
        ProfessorBuscarView sut = new ProfessorBuscarView();

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchProfessor(any(Professor.class))).thenReturn(new ArrayList<Professor>());
        sut.registerFacade(facadeMock);

        JButton searchButton = ((ProfessorBuscarPanel) sut.getGui()).getBuscar();

        try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
            // Act
            searchButton.doClick();

            // Assert
            optionPaneMock.verify(
                    () -> JOptionPane.showMessageDialog(
                            any(Component.class),
                            eq("Professor não encontrado!"),
                            anyString(),
                            eq(JOptionPane.ERROR_MESSAGE)),
                    times(1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5 })
    public void Buscar_AposBuscarProfessoresValidos_TabelaDeveConterOsProfessores(int professoresAmount) {
        // Arrange
        List<Professor> professoresSearchMock = new ProfessorMock().getProfessoresMock(professoresAmount);

        AppFacade facadeMock = mock(AppFacade.class); // Mock facade
        when(facadeMock.searchProfessor(any(Professor.class))).thenReturn(professoresSearchMock);

        JTextField textFieldNomeSpy = spy(new JTextField(faker.name().fullName()));
        JTextField textFieldRegistroSpy = spy(new JTextField(Long.toString(faker.random().nextLong())));

        try (MockedConstruction<ProfessorBuscarPanel> mocked = mockConstruction(
                ProfessorBuscarPanel.class,
                GetProfessorBuscarPanelMock(textFieldNomeSpy, textFieldRegistroSpy))) {

            ProfessorBuscarView sut = new ProfessorBuscarView();
            sut.registerFacade(facadeMock);
            JButton searchButton = ((ProfessorBuscarPanel) sut.getGui()).getBuscar();

            // Act
            searchButton.doClick();

            // Assert
            assertThat(professorTableMock.getRowCount()).isEqualTo(professoresAmount);

            for (int i = 0; i < professorTableMock.getRowCount(); i++) {
                String registroColumnValue = professorTableMock.getValueAt(i, 0).toString();
                String nomeColumnValue = professorTableMock.getValueAt(i, 1).toString();

                assertThat(registroColumnValue)
                        .isEqualTo(Long.toString(professoresSearchMock.get(i).getFiliado().getId()));
                assertThat(nomeColumnValue).isEqualTo(professoresSearchMock.get(i).getFiliado().getNome());
            }
        }
    }

    private MockInitializer<ProfessorBuscarPanel> GetProfessorBuscarPanelMock(JTextField textFieldNomeSpy, JTextField textFieldRegistroSpy)
    {
        return (professorPanelMock, context) -> 
        {
            // Mock campos de busca
            BuscaCamposPanel buscaCampoPanelMock = mock(BuscaCamposPanel.class);
            when(buscaCampoPanelMock.getNome()).thenReturn(textFieldNomeSpy);
            when(buscaCampoPanelMock.getRegistroFpij()).thenReturn(textFieldRegistroSpy);
            when(professorPanelMock.getBuscaCamposPanel()).thenReturn(buscaCampoPanelMock);

            // Mock botão de busca
            JButton buttonMock = spy(new JButton());
            doCallRealMethod().when(buttonMock).addActionListener(any(ProfessorBuscarView.BuscarActionHandler.class));
            when(professorPanelMock.getBuscar()).thenReturn(buttonMock);

            // Mock tabela de professores
            when(professorPanelMock.getProfessores()).thenReturn(professorTableMock);
        };
    }
}
