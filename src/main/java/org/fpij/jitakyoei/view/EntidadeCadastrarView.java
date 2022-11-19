package org.fpij.jitakyoei.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.validator.EntidadeValidator;
import org.fpij.jitakyoei.view.forms.EntidadeForm;
import org.fpij.jitakyoei.view.gui.EntidadeCadastrarPanel;

public class EntidadeCadastrarView implements ViewComponent {

	private EntidadeCadastrarPanel gui;
	private AppFacade facade;
	private EntidadeForm entidadeForm;
	private MainAppView parent;

	public EntidadeCadastrarView(MainAppView parent) {
		this.parent = parent;
		gui = new EntidadeCadastrarPanel();
		gui.getCancelar().addActionListener(new CancelarActionHandler());
		gui.getCadastrarEntidade().addActionListener(new CadastrarActionHandler());
		entidadeForm = new EntidadeForm(gui.getEntidadePanel());
		gui.setVisible(true);
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
	 * Classe interna responsável por responder aos cliques no botão "Cadastrar".
	 * 
	 * @author Aécio
	 */
	public class CadastrarActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
                                Entidade entidade = entidadeForm.getEntidade();
                                EntidadeValidator validator = new EntidadeValidator();
                                String result = validator.validateMissingFields(entidade);
                                
                                if (result.equals("")){
                                    
                                    
                                    if (validator.validate(entidade)){
                                        facade.createEntidade(entidadeForm.getEntidade());
                                        JOptionPane.showMessageDialog(gui, "Entidade cadastrada com sucesso!");
                                        parent.removeTabPanel(gui);
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(gui, "Campos NOME/CNPJ/TELEFONE possuem formato incorreto!");
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
