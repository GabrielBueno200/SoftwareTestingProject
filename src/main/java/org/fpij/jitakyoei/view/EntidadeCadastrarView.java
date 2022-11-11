package org.fpij.jitakyoei.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Entidade;
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
                                String regexCnpj = "^([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})$";
                                String regexTelefone = "^\\([0-9]{2}\\)[0-9]{4,5}-[0-9]{4}$";
                                String regexNome = "^[a-zA-Z\\s]+$";
                                Matcher matchCnpj = Pattern.compile(regexCnpj).matcher(entidade.getCnpj());
                                Matcher matchTelefone1 = Pattern.compile(regexTelefone).matcher(entidade.getTelefone1());
                                Matcher matchNome = Pattern.compile(regexNome).matcher(entidade.getNome());
                                if (entidade.getNome().isBlank() && entidade.getCnpj().isBlank() && entidade.getTelefone1().isBlank()){
                                    if (matchCnpj.matches() && matchTelefone1.matches() && matchNome.matches()){
                                        facade.createEntidade(entidadeForm.getEntidade());
                                        JOptionPane.showMessageDialog(gui, "Entidade cadastrada com sucesso!");
                                        parent.removeTabPanel(gui);
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(gui, "Campos NOME/CNPJ/TELEFONE possuem formato incorreto!");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(gui, "Campos obrigatórios não preenchidos!");
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
