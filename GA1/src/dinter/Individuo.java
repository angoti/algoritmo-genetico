package dinter;

public class Individuo {

	private byte[] cromossomo;
	private int tamanho;
	private double granularidade;

	public byte[] getCromossomo() {
		return cromossomo;
	}

	public void setCromossomo(byte[] cromossomo) {
		this.cromossomo = cromossomo;
	}

	public Individuo(int tamanho, double granularidade) {
		this.tamanho = tamanho;
		this.granularidade = granularidade;
		cromossomo = new byte[tamanho];
		for (int i = 0; i < tamanho; i++) {
			if (Math.random() >= 0.5)
				cromossomo[i] = 0;
			else
				cromossomo[i] = 1;
		}
	}

	public Individuo(Individuo i) {
		this.cromossomo = copiaCromossomo(i.getCromossomo());
		this.tamanho = i.tamanho;
		this.granularidade = i.granularidade;
	}

	private byte[] copiaCromossomo(byte[] cromossomo) {
		byte[] copia = new byte[cromossomo.length];
		for (int j = 0; j < cromossomo.length; j++) {
			copia[j] = cromossomo[j];
		}
		return copia;
	}

	public double funcaoAptidao() {
		double x = valorReal();
		return Math.abs(x * Math.sin(Math.sqrt(Math.abs(x))));
	}

	public int valorDecimal() {
		int decimal = 0;
		for (int i = tamanho - 1; i >= 0; i--) {
			decimal += (int) Math.pow(2, i) * cromossomo[tamanho - 1 - i];
		}
		return decimal;
	}

	public double valorReal() {
		return valorDecimal() * granularidade;
	}

	public void imprime() {
		for (int i = 0; i < cromossomo.length; i++) {
			System.out.print(cromossomo[i] + " ");
		}
		// System.out.print("\nValor decimal: " + valorDecimal());
		// System.out.print(" - Valor real: " + valorReal());
		System.out.println(" - Aptidão: " + funcaoAptidao());
	}

	public static void main(String[] args) {
		Individuo i = new Individuo(10, 0.5);
		Individuo j = new Individuo(i);

	}

	void mutacao() {
	}
}
