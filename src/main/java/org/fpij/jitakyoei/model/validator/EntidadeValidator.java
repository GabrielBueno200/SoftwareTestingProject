/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.fpij.jitakyoei.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Entidade;

/**
 *
 * @author henri
 */
public class EntidadeValidator implements Validator<Entidade>{

    @Override
    public boolean validate(Entidade obj) {
        String regexCnpj = "^([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})$";
        String regexTelefone = "^\\([0-9]{2}\\)[0-9]{4,5}-[0-9]{4}$";
        String regexNome = "^[a-zA-Z\\s]+$";
        Matcher matchCnpj = Pattern.compile(regexCnpj).matcher(obj.getCnpj());
        Matcher matchTelefone1 = Pattern.compile(regexTelefone).matcher(obj.getTelefone1());
        Matcher matchNome = Pattern.compile(regexNome).matcher(obj.getNome());
        
        return matchCnpj.matches() && matchNome.matches();
    }
    
    public String validateMissingFields(Entidade obj) {
        if (obj.getCnpj().isBlank()) return "O campo 'CNPJ' é obrigatório";
        if (obj.getNome().isBlank()) return "O campo 'nome' é obrigatório";
       return "";                      
    }
}
