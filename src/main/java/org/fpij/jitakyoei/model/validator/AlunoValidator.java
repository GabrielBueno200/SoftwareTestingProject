package org.fpij.jitakyoei.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fpij.jitakyoei.model.beans.Aluno;

public class AlunoValidator implements Validator<Aluno> {
	public boolean validate(Aluno obj) {
            String regexCpf = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$";
            String regexCbj = "^[0-9]+$";
            String regexEmail = "^\\w+@\\w+.com$";
            String regexNome = "^[a-zA-Z\\s]+$";
            Matcher matchCpf = Pattern.compile(regexCpf).matcher(obj.getFiliado().getCpf());
            Matcher matchEmail = Pattern.compile(regexEmail).matcher(obj.getFiliado().getEmail());
            Matcher matchNome = Pattern.compile(regexNome).matcher(obj.getFiliado().getNome());
            Matcher matchCbj = Pattern.compile(regexCbj).matcher(obj.getFiliado().getRegistroCbj());
            
            return matchCpf.matches() && matchEmail.matches() && matchNome.matches() && matchCbj.matches();
	}
}