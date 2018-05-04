package dinter;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

public class Tomassini {

	void ag(int nIndividuos, int nGenes, double gr) throws FileNotFoundException {
		// redireciona a saida padr�o para um arquivo
		System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("log.txt")), true));
		Populacao p = new Populacao(nIndividuos, nGenes, gr);
		System.out.println("Popula��o inicial\n");
		p.dadosPopulacao();
		p.dadosGeracao();
		System.out.println("----------------------------------------------------");
		int cont = 0;
		while (cont < 25) {
			System.out.println("\n\n Gera��o " + cont + " ***********");
			p.selecaoRoleta();
			System.out.println("\nPopula��o depois da sele��o - roleta\n");
			p.dadosPopulacao();
			System.out.println("----------------------------------------------------");
			p.cruzamento(0.60);
			System.out.println("\nPopula��o depois do cruzamento\n");
			p.dadosPopulacao();
			System.out.println("----------------------------------------------------");
			p.mutacao(0.01);
			System.out.println("\n" + "Popula��o depois da muta��o\n");
			p.dadosPopulacao();
			cont++;
		}
		Grafico.createAndShowGui(p);
	}

	void ag() throws FileNotFoundException {
		int numeroIndividuos = Integer.parseInt(JOptionPane.showInputDialog("Digite o n�mero de indiv�duos"));
		int nGenes = Integer.parseInt(JOptionPane.showInputDialog("Digite o n�mero de genes do indiv�duo"));
		double granularidade = Double.parseDouble(JOptionPane.showInputDialog("Digite a granularidade da representa��o do indiv�duo"));
		int numeroDeGeracoes = Integer.parseInt(JOptionPane.showInputDialog("Digite o n�mero de gera��es"));

		// redireciona a saida padr�o para um arquivo
		PrintStream stdout = System.out;
		System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("log.txt")), true));
		Populacao p = new Populacao(numeroIndividuos, nGenes, granularidade);
		System.out.println("Popula��o inicial\n");
		p.dadosPopulacao();
		p.dadosGeracao();
		System.out.println("----------------------------------------------------");
		int cont = 0;
		while (cont < numeroDeGeracoes) {
			System.out.println("\n\n Gera��o " + cont + " *******");
			p.selecaoRoleta();
			System.out.println("\nPopula��o depois da sele��o - roleta\n");
			p.dadosPopulacao();
			System.out.println("----------------------------------------------------");
			p.cruzamento(0.60);
			System.out.println("\nPopula��o depois do cruzamento\n");
			p.dadosPopulacao();
			System.out.println("----------------------------------------------------");
			p.mutacao(0.01);
			System.out.println("\n" + "Popula��o depois da muta��o\n");
			p.dadosPopulacao();
			cont++;
		}
		System.setOut(stdout);
		Grafico.createAndShowGui(p);
	}

	public static void main(String[] args) throws FileNotFoundException {
		// (new Tomassini()).ag(50, 10, 0.5);
		new Tomassini().ag();
	}

}
