package dinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Populacao {

	private int numeroDeIndividuos, numeroDeGenesDoIndividuo;
	private double granularidade;
	private Individuo[] geracao, geracao1;
	private List<Individuo[]> geracoes;
	private Random randomico = new Random();

	void cruzamento(double probabilidadeCruzamento) {
		for (int i = 0; i < geracao.length; i = i + 2) {
			double r = randomico.nextDouble();
			if (r < probabilidadeCruzamento) {
				byte[] i1 = geracao[i].getCromossomo();
				byte[] i2 = geracao[i + 1].getCromossomo();
				int posicaoCruzamento = randomico.nextInt(9) + 1;
				for (int j = posicaoCruzamento; j < numeroDeGenesDoIndividuo; j++) {
					byte gene = i1[j];
					i1[j] = i2[j];
					i2[j] = gene;
				}
			}
		}
	}

	public int getNumeroDeIndividuos() {
		return numeroDeIndividuos;
	}

	public void setNumeroDeIndividuos(int numeroDeIndividuos) {
		this.numeroDeIndividuos = numeroDeIndividuos;
	}

	public int getNumeroDeGenesDoIndividuo() {
		return numeroDeGenesDoIndividuo;
	}

	public void setNumeroDeGenesDoIndividuo(int numeroDeGenesDoIndividuo) {
		this.numeroDeGenesDoIndividuo = numeroDeGenesDoIndividuo;
	}

	public double getGranularidade() {
		return granularidade;
	}

	public void setGranularidade(double granularidade) {
		this.granularidade = granularidade;
	}

	public Individuo[] getGeracao() {
		return geracao;
	}

	public void setGeracao(Individuo[] geracao) {
		this.geracao = geracao;
	}

	public List<Individuo[]> getGeracoes() {
		return geracoes;
	}

	public void setGeracoes(List<Individuo[]> geracoes) {
		this.geracoes = geracoes;
	}

	public Populacao(int numeroDeIndividuos, int numeroDeGenesDoIndividuo, double granularidade) {
		this.numeroDeGenesDoIndividuo = numeroDeGenesDoIndividuo;
		this.granularidade = granularidade;
		this.numeroDeIndividuos = numeroDeIndividuos;
		geracoes = new ArrayList<Individuo[]>();
		criaNovaGeracao();
	}

	private void criaNovaGeracao() {
		geracao = new Individuo[numeroDeIndividuos];
		for (int i = 0; i < numeroDeIndividuos; i++) {
			geracao[i] = new Individuo(numeroDeGenesDoIndividuo, granularidade);
		}
		geracoes.add(geracao);
	}

	void selecaoRoleta() {

		// Calcula o somatório dos valores da função de aptidão de todos os
		// indivíduos da geração atual
		double somatorioAptidao = 0.0;
		for (Individuo i : geracao) {
			somatorioAptidao += i.funcaoAptidao();
		}

		// Calcula a probabilidade de cada indivíduo
		double[] probabilidade = new double[numeroDeIndividuos];
		for (int i = 0; i < probabilidade.length; i++) {
			probabilidade[i] = (geracao[i].funcaoAptidao() / somatorioAptidao);
		}

		// Calcula a probabilidade cumulativa para cada indivíduo
		double[] probabilidadeCumulativa = new double[numeroDeIndividuos];
		for (int i = 0; i < probabilidadeCumulativa.length; i++) {
			double soma = 0.0;
			for (int j = 0; j <= i; j++) {
				soma += probabilidade[j];
			}
			probabilidadeCumulativa[i] = soma;
			// System.out.println(i+" - "+soma);
		}
		// Gera a nova geração usando seleção por roleta
		// a variável geracao1 conterá 50 indivíduos, que serão usados para o
		// cruzamento
		geracao1 = new Individuo[numeroDeIndividuos];
		double r = 0.0;
		int indiceEscolhido = 0;
		for (int i = 0; i < geracao1.length; i++) {
			r = randomico.nextDouble();
			for (int j = 0; j < probabilidadeCumulativa.length; j++) {
				if (r <= probabilidadeCumulativa[j]) {
					// geracao1[i] = geracao[j];
					// copia o indivíduo da geração antiga para a nova geração,
					// conforme o sorteio da roleta
					geracao1[i] = new Individuo(geracao[j]);
					break;
				}
			}
		}
		geracao = geracao1;
		geracoes.add(geracao);
	}

	public void mutacao(double probabilidadeMutacao) {
		double r = 0;
		for (Individuo i : geracao) {
			for (int j = 0; j < numeroDeGenesDoIndividuo; j++) {
				r = randomico.nextDouble();
				if (r < probabilidadeMutacao) {
					// System.out.println("+++++++++ ocorrência de mutação
					// +++++++++++++++++++++++++++++++++++++");
					i.getCromossomo()[j] = i.getCromossomo()[j] == 0 ? (byte) 1 : (byte) 0;
				}
			}
		}
	}

	public static void main(String[] args) {
		Random randomico = new Random();
		for (int i = 0; i < 100; i++)
			System.out.println(randomico.nextInt(9) + 1);
	}

	Individuo melhorIndividuo() {
		Individuo melhor = geracao[0];
		for (int i = 1; i < geracao.length; i++) {
			if (geracao[i].funcaoAptidao() > melhor.funcaoAptidao())
				melhor = geracao[i];
		}
		return melhor;
	}

	void dadosPopulacao() {
		for (int i = 0; i < getGeracao().length; i++) {
			getGeracao()[i].imprime();
		}
		Individuo ind = melhorIndividuo();
		System.out.println("\n\nMelhor indivíduo");
		ind.imprime();
	}

	void dadosGeracao() {
		Individuo melhor = geracao[0];
		double media = 0;
		for (int i = 0; i < geracao.length; i++) {
			media += geracao[i].funcaoAptidao();
			if (geracao[i].funcaoAptidao() > melhor.funcaoAptidao())
				melhor = geracao[i];
		}
		media /= geracao.length;
		System.out.println("Melhor individuo: " + melhor.funcaoAptidao());
		System.out.println("Média: " + media);
	}

}
