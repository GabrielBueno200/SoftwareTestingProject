package org.fpij.jitakyoei.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.model.validator.ProfessorValidator;
import org.fpij.jitakyoei.view.forms.ProfessorForm;
import org.fpij.jitakyoei.view.gui.ProfessorAtualizarPanel;

public class ProfessorAtualizarView implements ViewComponent {
	private ProfessorAtualizarPanel gui;
	private ProfessorForm professorForm;
	private AppFacade facade;
	private MainAppView parent;

	public ProfessorAtualizarView(MainAppView parent, Professor professor) {
		this.parent = parent;
		gui = new ProfessorAtualizarPanel();
		professorForm = new ProfessorForm(gui.getProfessorPanel(), professor);
		gui.getCancelar().addActionListener(new CancelarActionHandler());
		gui.getAtualizar().addActionListener(new AtualizarActionHandler());
	}

	@Override
	public JPanel getGui() {
		return gui;
	}

	@Override
	public void registerFacade(AppFacade fac) {
		this.facade = fac;
	}

	/**
	 * Classe interna responsável por responder aos cliques no botão "Atualizar".
	 * 
	 * @author Aécio
	 */
	public class AtualizarActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Professor professor = professorForm.getProfessor();
			try {
                                ProfessorValidator validator = new ProfessorValidator();
                                String result = validator.validateMissingFields(professor);
                                if (result.equals("")){
                                    if (validator.validate(professor)){
                                        facade.updateProfessor(professor);
                                        JOptionPane.showMessageDialog(gui, "Professor atualizado com sucesso!");
                                        parent.removeTabPanel(gui);
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(gui, "Campos NOME/CPF/CBJ possuem formato incorreto!");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(gui, result);
                                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Classe interna responsável por responder aos cliques no botão "Cancelar".
	 * 
	 * @author Aécio
	 */
	public class CancelarActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			parent.removeTabPanel(gui);
		}
	}
}
