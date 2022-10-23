package org.fpij.jitakyoei.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class AlunoBuscarViewTest {
    @Test
    public void Buscar_AoPesquisarAlunoInexistente_InformarQueOAlunoNaoFoiEncontrado() {
        // Arrange
        AlunoBuscarView sut = new AlunoBuscarView();

        AppFacade facadeMock = mock(AppFacade.class);
        when(facadeMock.searchAluno(any(Aluno.class))).thenReturn(new ArrayList<Aluno>());
        sut.registerFacade(facadeMock);

        try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(JOptionPane.class)) {
            // Act
            sut.buscar();

            // Assert
            optionPaneMock.verify(
                    () -> JOptionPane.showMessageDialog(
                            any(Component.class),
                            eq("Aluno não encontrado!")),
                    times(1));
        }
    }
}