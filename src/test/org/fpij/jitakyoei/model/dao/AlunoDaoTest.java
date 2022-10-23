package org.fpij.jitakyoei.model.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Endereco;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.util.DatabaseManager;
import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AlunoDaoTest {

	private static DAO<Aluno> alunoDao;
	private static Aluno aluno;
	private static Entidade entidade;
	private static Endereco endereco;
	private static Filiado f1;
	private static Filiado filiadoProf;
	private static Professor professor;

	@BeforeAll
	public static void setUp() {
		DatabaseManager.setEnviroment(DatabaseManager.TEST);
		f1 = new Filiado();
		f1.setNome("Aécio");
		f1.setCpf("036.464.453-27");
		f1.setDataNascimento(new Date());
		f1.setDataCadastro(new Date());
		f1.setId(1332L);

		endereco = new Endereco();
		endereco.setBairro("Dirceu");
		endereco.setCep("64078-213");
		endereco.setCidade("Teresina");
		endereco.setEstado("PI");
		endereco.setRua("Rua Des. Berilo Mota");

		filiadoProf = new Filiado();
		filiadoProf.setNome("Professor");
		filiadoProf.setCpf("036.464.453-27");
		filiadoProf.setDataNascimento(new Date());
		filiadoProf.setDataCadastro(new Date());
		filiadoProf.setId(3332L);
		filiadoProf.setEndereco(endereco);

		professor = new Professor();
		professor.setFiliado(filiadoProf);

		entidade = new Entidade();
		entidade.setEndereco(endereco);
		entidade.setNome("Academia 1");
		entidade.setTelefone1("(086)1234-5432");

		aluno = new Aluno();
		aluno.setFiliado(f1);
		aluno.setProfessor(professor);
		aluno.setEntidade(entidade);

		alunoDao = new DAOImpl<Aluno>(Aluno.class);
	}

	public static void clearDatabase() {
		List<Aluno> all = alunoDao.list();
		for (Aluno each : all) {
			alunoDao.delete(each);
		}

		assertThat(alunoDao.list().size()).isZero();
	}

	@Test
	public void testSalvarAlunoComAssociassoes() throws Exception {
		clearDatabase();

		alunoDao.save(aluno);
		assertThat("036.464.453-27").isEqualTo(alunoDao.get(aluno).getFiliado().getCpf());
		assertThat("Aécio").isEqualTo(alunoDao.get(aluno).getFiliado().getNome());
		assertThat("Professor").isEqualTo(alunoDao.get(aluno).getProfessor().getFiliado().getNome());
		assertThat("Dirceu").isEqualTo(alunoDao.get(aluno).getProfessor().getFiliado().getEndereco().getBairro());
	}

	@Test
	public void updateAluno() throws Exception {
		clearDatabase();
		assertThat(alunoDao.list().size()).isZero();

		alunoDao.save(aluno);
		assertThat(alunoDao.list().size()).isEqualTo(1);
		assertThat("Aécio").isEqualTo(aluno.getFiliado().getNome());

		Aluno a1 = alunoDao.get(aluno);
		a1.getFiliado().setNome("TesteUpdate");
		alunoDao.save(a1);

		Aluno a2 = alunoDao.get(a1);
		assertThat("TesteUpdate").isEqualTo(a2.getFiliado().getNome());
		assertThat(alunoDao.list().size()).isEqualTo(1);
	}

	@Test
	public void testListarEAdicionarAlunos() {
		int qtd = alunoDao.list().size();

		alunoDao.save(new Aluno());
		assertThat(alunoDao.list().size()).isEqualTo(qtd + 1);

		alunoDao.save(new Aluno());
		assertThat(alunoDao.list().size()).isEqualTo(qtd + 2);

		alunoDao.save(new Aluno());
		assertThat(alunoDao.list().size()).isEqualTo(qtd + 3);

		alunoDao.save(new Aluno());
		assertThat(alunoDao.list().size()).isEqualTo(qtd + 4);

		clearDatabase();
		assertThat(alunoDao.list().size()).isZero();

		alunoDao.save(new Aluno());
		assertThat(alunoDao.list().size()).isEqualTo(1);
	}

	@Test
	public void testSearchAluno() throws Exception {
		clearDatabase();
		alunoDao.save(aluno);

		Filiado f = new Filiado();
		f.setNome("Aécio");
		Aluno a = new Aluno();
		a.setFiliado(f);

		List<Aluno> result = alunoDao.search(a);
		assertThat(result.size()).isEqualTo(1);
		assertThat("036.464.453-27").isEqualTo(result.get(0).getFiliado().getCpf());

		clearDatabase();
		assertThat(alunoDao.search(a).size()).isZero();
	}

	@AfterClass
	public static void closeDatabase() {
		clearDatabase();
		DatabaseManager.close();
	}
}