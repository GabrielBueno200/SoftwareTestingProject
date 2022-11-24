package org.fpij.jitakyoei.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fpij.jitakyoei.model.beans.Aluno;

public class AlunoValidator implements Validator<Aluno> {
        @Override
	public boolean validate(Aluno obj) {
            String regexCpf = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}";
            String regexCbj = "^[0-9]+$";
            String regexEmail = "^\\w+@\\w+.com$";
            String regexNome = "^[a-zA-Z\\s]+$";
            Matcher matchCpf = Pattern.compile(regexCpf).matcher(obj.getFiliado().getCpf());
            Matcher matchEmail = Pattern.compile(regexEmail).matcher(obj.getFiliado().getEmail());
            Matcher matchNome = Pattern.compile(regexNome).matcher(obj.getFiliado().getNome());
            Matcher matchCbj = Pattern.compile(regexCbj).matcher(obj.getFiliado().getRegistroCbj());

            return matchCpf.matches() && matchNome.matches() && matchCbj.matches();
	}

        public String validateMissingFields(Aluno obj) {
            if (obj.getFiliado().getCpf().isBlank()) return "O campo 'CPF' é obrigatório";
            if (obj.getFiliado().getNome().isBlank()) return "O campo 'nome' é obrigatório";
            if (obj.getFiliado().getRegistroCbj().isBlank()) return "O campo 'Registro Cbj' é obrigatório";
           return "";                      
	}
}