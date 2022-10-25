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
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.view.gui.AlunoBuscarPanel;
import org.fpij.jitakyoei.view.gui.BuscaCamposPanel;
import org.fpijk.jutakyoei.mocks.AlunoEmptyTableMock;
import org.fpijk.jutakyoei.mocks.AlunoMock;
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

public class AlunoBuscarViewTest {
    private static JTable alunoTableMock;
    private static Faker faker;
    private static FakeValuesService fakeValuesService;

    @BeforeAll
    public static void setUp() {
        alunoTableMock = new AlunoEmptyTableMock();
        faker = new Faker(new Locale("pt-BR"));
        fakeValuesService = new FakeValuesService(
                new Locale("pt-BR"), new RandomService());
    }

    @Test
    public void Buscar_AoPesquisarAlunoInexistente_ExibirAlertaAlunoNaoEncontrado() {
        // Arrange
        AlunoBuscarView sut = new AlunoBuscarView();

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchAluno(any(Aluno.class))).thenReturn(new ArrayList<Aluno>());
        sut.registerFacade(facadeMock);

        JButton searchButton = ((AlunoBuscarPanel) sut.getGui()).getBuscar();

        try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
            // Act
            searchButton.doClick();

            // Assert
            optionPaneMock.verify(
                    () -> JOptionPane.showMessageDialog(
                            any(Component.class),
                            eq("Aluno não encontrado!"),
                            anyString(),
                            eq(JOptionPane.ERROR_MESSAGE)),
                    times(1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5 })
    public void Buscar_AposBuscarAlunosValidos_TabelaDeveConterOsAlunos(int alunosAmount) {
        // Arrange
        List<Aluno> alunosSearchMock = new AlunoMock().getAlunosMock(alunosAmount);

        AppFacade facadeMock = mock(AppFacade.class); // Mock facade
        when(facadeMock.searchAluno(any(Aluno.class))).thenReturn(alunosSearchMock);

        JTextField textFieldNomeSpy = spy(new JTextField(faker.name().fullName()));
        JTextField textFieldRegistroSpy = spy(new JTextField(Long.toString(faker.random().nextLong())));

        try (MockedConstruction<AlunoBuscarPanel> mocked = mockConstruction(
                AlunoBuscarPanel.class,
                GetAlunoBuscarPanelMock(textFieldNomeSpy, textFieldRegistroSpy))) {

            AlunoBuscarView sut = new AlunoBuscarView();
            sut.registerFacade(facadeMock);
            JButton searchButton = ((AlunoBuscarPanel) sut.getGui()).getBuscar();

            // Act
            searchButton.doClick();

            // Assert
            assertThat(alunoTableMock.getRowCount()).isEqualTo(alunosAmount);

            for (int i = 0; i < alunoTableMock.getRowCount(); i++) {
                String registroColumnValue = alunoTableMock.getValueAt(i, 0).toString();
                String nomeColumnValue = alunoTableMock.getValueAt(i, 1).toString();
                String professorColumnValue = alunoTableMock.getValueAt(i, 2).toString();
                String entidadeColumnValue = alunoTableMock.getValueAt(i, 3).toString();

                assertThat(registroColumnValue).isEqualTo(Long.toString(alunosSearchMock.get(i).getFiliado().getId()));
                assertThat(nomeColumnValue).isEqualTo(alunosSearchMock.get(i).getFiliado().getNome());
                assertThat(professorColumnValue)
                        .isEqualTo(alunosSearchMock.get(i).getProfessor().getFiliado().getNome());
                assertThat(entidadeColumnValue).isEqualTo(alunosSearchMock.get(i).getEntidade().getNome());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5 })
    public void Buscar_AposBuscarAlunosValidos_ListaDeAlunosDeveEstarPreenchida(int alunosAmount) {
        // Arrange
        List<Aluno> alunosSearchMock = new AlunoMock().getAlunosMock(alunosAmount);

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchAluno(any(Aluno.class))).thenReturn(alunosSearchMock);

        AlunoBuscarView sut = new AlunoBuscarView();
        sut.registerFacade(facadeMock);
        JButton searchButton = ((AlunoBuscarPanel) sut.getGui()).getBuscar();

        // Act
        searchButton.doClick();

        // Assert
        assertThat(sut.getAlunoList().size()).isEqualTo(alunosAmount);
        assertThat(sut.getAlunoList()).isEqualTo(alunosSearchMock);
    }

    @Test
    public void Buscar_InserirRegistroComLetras_ExibirAlertaDeRegistroInvalido() {
        // Arrange
        JTextField textFieldRegistroSpy = spy(new JTextField(fakeValuesService.regexify("[a-zA-Z]+")));
        JTextField textFieldNomeSpy = spy(new JTextField(""));

        try (MockedConstruction<AlunoBuscarPanel> mocked = mockConstruction(
                AlunoBuscarPanel.class,
                GetAlunoBuscarPanelMock(textFieldNomeSpy, textFieldRegistroSpy))) {

            AlunoBuscarView sut = new AlunoBuscarView();
            JButton searchButton = ((AlunoBuscarPanel) sut.getGui()).getBuscar();

            try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
                // Act
                searchButton.doClick();

                // Assert
                optionPaneMock.verify(
                        () -> JOptionPane.showMessageDialog(
                                any(Component.class),
                                eq("Nº de Registro inválido! No resgistro só pode haver números.")),
                        times(1));
            }
        }
    }

    private MockInitializer<AlunoBuscarPanel> GetAlunoBuscarPanelMock(JTextField textFieldNomeSpy, JTextField textFieldRegistroSpy)
    {
        return (alunoPanelMock, context) -> 
        {
            // Mock campos de busca
            BuscaCamposPanel buscaCampoPanelMock = mock(BuscaCamposPanel.class); 
            when(buscaCampoPanelMock.getNome()).thenReturn(textFieldNomeSpy);
            when(buscaCampoPanelMock.getRegistroFpij()).thenReturn(textFieldRegistroSpy);
            when(alunoPanelMock.getBuscaCamposPanel()).thenReturn(buscaCampoPanelMock);

            // Mock botão de busca
            JButton buttonSpy = spy(new JButton());
            doCallRealMethod().when(buttonSpy).addActionListener(any(AlunoBuscarView.BuscarActionHandler.class));
            when(alunoPanelMock.getBuscar()).thenReturn(buttonSpy);

            // Mock tabela de alunos
            when(alunoPanelMock.getAlunoTable()).thenReturn(alunoTableMock);
        };
    }
}