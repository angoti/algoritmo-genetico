package dinter;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class Grafico extends JPanel {
	private static final int MAX_SCORE = 512;
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	private static final int BORDER_GAP = 30;
	private static final Color GRAPH_COLOR = Color.green;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
	private static final int GRAPH_POINT_WIDTH = 5;
	private static final int Y_HATCH_CNT = 10;
	private List<Double> scores, populacao, popInicial;

	public Grafico(List<Double> scores, List<Double> populacao, List<Double> popInicial) {
		this.scores = scores;
		this.populacao = populacao;
		this.popInicial = popInicial;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.size() - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

		List<Point> graphPoints = new ArrayList<Point>();
		for (int i = 0; i < scores.size(); i++) {
			int x1 = (int) (i * xScale + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
			graphPoints.add(new Point(x1, y1));
		}

		List<Point> graphPoints1 = new ArrayList<Point>();
		for (int i = 0; i < populacao.size(); i++) {
			int x1 = (int) (i * xScale + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - populacao.get(i)) * yScale + BORDER_GAP);
			graphPoints1.add(new Point(x1, y1));
		}

		List<Point> graphPoints2 = new ArrayList<Point>();
		for (int i = 0; i < popInicial.size(); i++) {
			int x1 = (int) (i * xScale + BORDER_GAP);
			int y1 = (int) ((MAX_SCORE - popInicial.get(i)) * yScale + BORDER_GAP);
			graphPoints2.add(new Point(x1, y1));
		}

		// create x and y axes
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		// create hatch marks for y axis.
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis
		for (int i = 0; i < scores.size() - 1; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
		}

		// plota a função
		g2.setColor(GRAPH_COLOR);
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < graphPoints.size() - 1; i++) {
			int x1 = graphPoints.get(i).x;
			int y1 = graphPoints.get(i).y;
			int x2 = graphPoints.get(i + 1).x;
			int y2 = graphPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);
		}

		// plota a última geração
		// g2.setStroke(oldStroke);
		g2.setColor(Color.red);
		for (int i = 0; i < graphPoints1.size(); i++) {
			int x = graphPoints1.get(i).x - GRAPH_POINT_WIDTH + 4 / 2;
			int y = graphPoints1.get(i).y - GRAPH_POINT_WIDTH + 4 / 2;
			int ovalW = GRAPH_POINT_WIDTH + 4;
			int ovalH = GRAPH_POINT_WIDTH + 4;
			if (y < 619)
				g2.fillOval(x, y, ovalW, ovalH);
		}

		// plota a geração inicial
		// g2.setStroke(oldStroke);
		g2.setColor(Color.blue);
		for (int i = 0; i < graphPoints2.size(); i++) {
			int x = graphPoints2.get(i).x - GRAPH_POINT_WIDTH / 2;
			int y = graphPoints2.get(i).y - GRAPH_POINT_WIDTH / 2;
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			if (y < 620)
				g2.fillOval(x, y, ovalW, ovalH);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	public static void createAndShowGui(Populacao p) {
		List<Double> scores = new ArrayList<Double>();
		for (int x = 0; x < 512; x++) {
			scores.add(Math.abs(x * Math.sin(Math.sqrt(Math.abs(x)))));
		}

		// lista de pontos da última geração
		List<Double> populacao = new ArrayList<Double>();
		for (int x = 0; x < 512; x++) {
			populacao.add(-1.0);
		}
		for (Individuo i : p.getGeracao()) {
			populacao.set((int) i.valorReal(), i.funcaoAptidao());
		}

		// lista de pontos da população inicial
		List<Double> popInicial = new ArrayList<Double>();
		for (int x = 0; x < 512; x++) {
			popInicial.add(-1.0);
		}
		for (Individuo i : p.getGeracoes().get(0)) {
			popInicial.set((int) i.valorReal(), i.funcaoAptidao());
		}

		Grafico mainPanel = new Grafico(scores, populacao, popInicial);

		JTextArea relatorio = new JTextArea(10, 26);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		relatorio.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JScrollPane scroll = new JScrollPane(relatorio);
		String line;

		try {
			FileReader fileReader = new FileReader("log.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				relatorio.append(line + "\n");
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Não foi possível abrir o arquivo");
		} catch (IOException ex) {
			System.out.println("Erro ao ler arquivo");
		}

		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.getContentPane().add(scroll, BorderLayout.LINE_START);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui(null);
			}
		});
	}
}
