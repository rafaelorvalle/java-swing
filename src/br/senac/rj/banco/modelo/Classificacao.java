package br.senac.rj.banco.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Classificacao {
	
	private int id;
	private Time time;
	private int pontuacao;
	private int vitorias;
	private int derrotas;
	private int empates;
	
	public Classificacao() {
		
	}
	
	public Classificacao(int id, Time time, int pontuacao, int vitorias, int derrotas, int empates) {
		this.id = id;
		this.time = time;
		this.pontuacao = pontuacao;
		this.vitorias = vitorias;
		this.derrotas = derrotas;
		this.empates = empates;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public int getVitorias() {
		return vitorias;
	}

	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}

	public int getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(int derrotas) {
		this.derrotas = derrotas;
	}

	public int getEmpates() {
		return empates;
	}

	public void setEmpates(int empates) {
		this.empates = empates;
	}
	
	

	public boolean cadastrarClassificacao(Time time, int pontuacao, int vitorias, int derrotas, int empates) {
		Connection conexao = null;
		try {
			conexao = Conexao.conectaBanco();
			String sql = "INSERT INTO classificacao(id_time, pontuacao, vitorias, derrotas, empates) VALUES (?, ?, ?, ?, ?);";
			// Prepara a consulta
			PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			// Define os par�metros da consulta
			
			ps.setInt(1, time.getId());
			ps.setInt(2, pontuacao);
			ps.setInt(3, vitorias);
			ps.setInt(4, derrotas);
			ps.setInt(5, empates);
			
			int totalRegistrosAfetados = ps.executeUpdate();
			if (totalRegistrosAfetados == 0) {
				System.out.println("N�o foi feito o cadastro!");
				return false;
			}
			System.out.println("Cadastro realizado!");
			return true;
		} catch (SQLException erro) {
			System.out.println("Erro ao cadastrar a classifica��o do time: " + erro.toString());
			return false;
		} finally {
			Conexao.fechaConexao(conexao);
		}
	}
	
	public boolean consultarClassificacao(Time time) {
		// Define a conex�o
		Connection conexao = null;
		try {
			conexao = Conexao.conectaBanco();
			// Define a consulta
			String sql = "select c.id, c.pontuacao, c.vitorias, c.derrotas, c.empates, t.id, t.nome, t.tecnico, t.estado, t.cidade from classificacao c JOIN time t ON c.id_time = t.id where c.id_time = ? ";
			// Prepara a consulta
			PreparedStatement ps = conexao.prepareStatement(sql);
			// Define os par�metros da consulta
			ps.setInt(1, time.getId());
			// Executa a consulta, resultando em um objeto da classe ResultSet
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) { // Verifica se n�o est� antes do primeiro registro
				System.out.println("Classifica��o n�o cadastrada!");
				return false; // Estudante n�o cadastrado n�o cadastrada
			} else {
				// Efetua a leitura do registro da tabela
				while (rs.next()) {
					this.id = rs.getInt("c.id");
					this.pontuacao = rs.getInt("c.pontuacao");
					this.derrotas = rs.getInt("c.derrotas");
					this.vitorias = rs.getInt("c.vitorias");
					this.empates = rs.getInt("c.empates");
					
					int idTime = rs.getInt("t.id");
				    String nomeTime = rs.getString("t.nome");
				    String nomeTecnico = rs.getString("t.tecnico");
				    String EstadoTime = rs.getString("t.estado");
				    String CidadeTime = rs.getString("t.cidade");
				    time = new Time(idTime, nomeTime, nomeTecnico, EstadoTime, CidadeTime);
				    this.time = time;
					
				}
				return true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao consultar a classifica��o do time: " + erro.toString());
			return false;
		} finally {
			Conexao.fechaConexao(conexao);
		}
	}
	
	public boolean atualizaClassificacao(Time time, int pontuacao, int vitorias, int derrotas, int empates) {
		if (!consultarClassificacao(time))
			return false;
		else {
			Connection conexao = null;
			try {
				// Define a conex�o
				conexao = Conexao.conectaBanco();
				// Define a consulta
				String sql = "UPDATE classificacao set pontuacao=?, vitorias=?, derrotas=?, empates=? where id_time=?";
				// Prepara a consulta
				PreparedStatement ps = conexao.prepareStatement(sql);
				// Define os par�metros da atualiza��o
				
				ps.setInt(1, pontuacao);
				ps.setInt(2, vitorias);
				ps.setInt(3, derrotas);
				ps.setInt(4, empates);
				ps.setInt(5, time.getId());
				int totalRegistrosAfetados = ps.executeUpdate();
				if (totalRegistrosAfetados == 0)
					System.out.println("N�o foi feita a atualiza��o!");
				else
					System.out.println("Atualiza��o realizada!");
				return true;
			} catch (SQLException erro) {
				System.out.println("Erro ao atualizar o cadastro da classificacao: " + erro.toString());
				return false;
			} finally {
				Conexao.fechaConexao(conexao);
			}
		}
	}
	
	public boolean deletarClassificacao(Time time) {
		// Define a conex�o
		Connection conexao = null;
		try {
			conexao = Conexao.conectaBanco();
			// Define a consulta
			String sql = "DELETE from classificacao where id_time=?";
			// Prepara a consulta
			PreparedStatement ps = conexao.prepareStatement(sql);
			// Define os par�metros da consulta
			ps.setInt(1, time.getId());
			int totalRegistrosAfetados = ps.executeUpdate();
			if (totalRegistrosAfetados == 0)
				System.out.println("N�o foi feita a exclus�o do time da classifica��o!");
			else
				System.out.println("Exclus�o realizada!");
				return true;
		} catch (SQLException erro) {
			System.out.println("Erro ao deletar o time da classifica��o: " + erro.toString());
			return false;
		} finally {
			Conexao.fechaConexao(conexao);
		}
	}
	
	public static List<Classificacao> obterListaClassificacaoDoBanco() {
        List<Classificacao> classificacoes = new ArrayList<>();

        Connection conexao = null;
        try {
            conexao = Conexao.conectaBanco();
            String sql = "select c.id, c.pontuacao, c.vitorias, c.derrotas, c.empates, t.id, t.nome, t.tecnico, t.estado, t.cidade from classificacao c JOIN time t ON t.id = c.id_time ORDER BY c.pontuacao DESC";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) { // Verifica se h� registros
                System.out.println("N�o h� times cadastrados!");
                return null;
            } else {
                // Efetua a leitura dos registros da tabela
                while (rs.next()) {
                	int id = rs.getInt("c.id");
                	int pontuacao = rs.getInt("c.pontuacao");
                	int vitorias = rs.getInt("c.vitorias");
                	int derrotas = rs.getInt("c.derrotas");
                	int empates = rs.getInt("c.empates");
                	
                	
                	int idTime = rs.getInt("t.id");
				    String nomeTime = rs.getString("t.nome");
				    String nomeTecnico = rs.getString("t.tecnico");
				    String EstadoTime = rs.getString("t.estado");
				    String CidadeTime = rs.getString("t.cidade");
				    Time time = new Time(idTime, nomeTime, nomeTecnico, EstadoTime, CidadeTime);
				    
				    Classificacao classif = new Classificacao(id, time, pontuacao, vitorias,derrotas,empates);
				    classificacoes.add(classif);
                    
                }
                return classificacoes;
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao consultar classifica��es dos times: " + erro.toString());
            return null;
        } finally {
            Conexao.fechaConexao(conexao);
        }
    }
	
	
	

}
