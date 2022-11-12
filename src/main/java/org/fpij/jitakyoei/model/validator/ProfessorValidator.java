package org.fpij.jitakyoei.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fpij.jitakyoei.model.beans.Professor;

/**
 *
 * @author henri
 */
public class ProfessorValidator implements Validator<Professor>{

    @Override
    public boolean validate(Professor obj) {
        String regexCpf = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$";
        String regexEmail = "^\\w+@\\w+.com$";
        String regexNome = "^[a-zA-Z\\s]+$";
        Matcher matchCpf = Pattern.compile(regexCpf).matcher(obj.getFiliado().getCpf());
        Matcher matchEmail = Pattern.compile(regexEmail).matcher(obj.getFiliado().getEmail());
        Matcher matchNome = Pattern.compile(regexNome).matcher(obj.getFiliado().getNome());
        
        return matchCpf.matches() && matchEmail.matches() && matchNome.matches();
    }
    
}
