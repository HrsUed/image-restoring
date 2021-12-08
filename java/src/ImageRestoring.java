import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ImageRestoring extends JFrame {
/*
 * **********************************************************************
 * 									fields
 * **********************************************************************
 */
	private JPanel control;					// 制御パネル
	private JPanel condition;				// 条件パネル
	private JPanel labeledOriginal;			// ラベル付き原画像パネル
	private JPanel labeledDegraded;			// ラベル付き劣化画像パネル
	private JPanel labeledRestored;			// ラベル付き修復画像パネル
	private JLabel labelOriginal;			// 原画像ラベル
	private JLabel labelDegraded;			// 劣化像ラベル
	private JLabel labelRestored;			// 修復画像ラベル
	private ImagePanel original;			// 原画像パネル
	private ImagePanel degraded;			// 劣化画像パネル
	private ImagePanel restored;			// 修復画像パネル
	private JPanel main;					// メインパネル

	private double revProb = 0.1;			// 反転確率
	private JTextField revProbField;		// 反転確率入力フィールド
	private JButton resetBtn;				// リセットボタン
	private JButton recreateBtn;			// 新規作成ボタン
	private JButton degradeBtn;				// 劣化ボタン
	private JButton restoreBtn;				// 修復ボタン

	private JRadioButton drawBtn;			// 線描画ラジオボタン
	private JRadioButton eraseBtn;			// 線消去らじをボタン
	private JRadioButton dotBtn;			// 点描画ラジオボタン
	private ButtonGroup drawGroup;			// ボタングループ

	private JLabel tempP;					// 西森温度の値を格納したのラベル
	private JTextField tempM;				// 修復温度のフィールド
	private JTextField exch;				// 交換定数
	private JTextField err;					// 許容誤差のフィールド
	private JLabel prob;					// 事後確率の値を格納したラベル


/*
 * **********************************************************************
 * 								constructors
 * **********************************************************************
 */
	private ImageRestoring() {
		setTitle("画像修復アプリ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 100, 1000, 700);

		control = new JPanel();

		control.setLayout(new GridLayout(5,2));
		control.add(new JLabel("劣化確率"));

		revProbField = new JTextField(Double.toString(revProb), 10);
		revProbField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}
			public void insertUpdate(DocumentEvent e){
				try {
					revProb = Double.parseDouble(revProbField.getText());
				} catch (NumberFormatException ex) {
					revProb = 0.0;
				}
				tempP.setText(Double.toString(1.0/getBetaP()));
			}
			public void removeUpdate(DocumentEvent e){
				try {
					revProb = Double.parseDouble(revProbField.getText());
				} catch (NumberFormatException ex) {
					revProb = 0.0;
				}
				tempP.setText(Double.toString(1.0/getBetaP()));
			}
		});
		control.add(revProbField);

		degradeBtn = new JButton("劣化させる");
		degradeBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				degrade();
			}
		});
		control.add(degradeBtn);

		restoreBtn = new JButton("画像修復");
		restoreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restore();
			}
		});
		control.add(restoreBtn);

		resetBtn = new JButton("デフォルト画像を表示");
		resetBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				initialize();
			}
		});
		control.add(resetBtn);

		recreateBtn = new JButton("画像を消す");
		recreateBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		control.add(recreateBtn);

		drawBtn = new JRadioButton("線を引く");
		drawBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (drawBtn.isSelected())
					original.setDrawType(DrawType.DRAW);
			}
		});
		eraseBtn = new JRadioButton("線を消す");
		eraseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (eraseBtn.isSelected())
					original.setDrawType(DrawType.ERASE);
			}
		});
		dotBtn = new JRadioButton("点を書く");
		dotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dotBtn.isSelected())
					original.setDrawType(DrawType.DOT);
			}
		});
		drawGroup = new ButtonGroup();
		drawGroup.add(drawBtn);
		drawGroup.add(eraseBtn);
		drawGroup.add(dotBtn);
		control.add(drawBtn);
		control.add(eraseBtn);
		control.add(dotBtn);

		condition = new JPanel();
		condition.setLayout(new GridLayout(5,2));
		tempP = new JLabel(Double.toString(1.0/getBetaP()));
		tempM = new JTextField("1.0", 10);
		exch = new JTextField("1.0", 10);
		err = new JTextField("1e-5", 10);
		prob = new JLabel("0.0");
		condition.add(new JLabel("西森温度"));
		condition.add(tempP);
		condition.add(new JLabel("修復温度"));
		condition.add(tempM);
		condition.add(new JLabel("交換定数（隣接ドットの結合度"));
		condition.add(exch);
		condition.add(new JLabel("誤差"));
		condition.add(err);
		condition.add(new JLabel("修復率"));
		condition.add(prob);

		GridBagLayout gbLayout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		labelOriginal = new JLabel("原画像");
		labelDegraded = new JLabel("劣化画像");
		labelRestored = new JLabel("修復画像");
		original = new ImagePanel(true);
		degraded = new ImagePanel(true);
		restored = new ImagePanel(true);
		original.defaultInitialize();
		labeledOriginal = new JPanel();
		labeledDegraded = new JPanel();
		labeledRestored = new JPanel();
		labeledOriginal.setLayout(gbLayout);
		labeledDegraded.setLayout(gbLayout);
		labeledRestored.setLayout(gbLayout);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbLayout.setConstraints(labelOriginal, gbc);
		gbLayout.setConstraints(labelDegraded, gbc);
		gbLayout.setConstraints(labelRestored, gbc);

		gbc.gridy = 1;
		gbLayout.setConstraints(original, gbc);
		gbLayout.setConstraints(degraded, gbc);
		gbLayout.setConstraints(restored, gbc);

		labeledOriginal.add(labelOriginal);
		labeledOriginal.add(original);

		labeledDegraded.add(labelDegraded);
		labeledDegraded.add(degraded);

		labeledRestored.add(labelRestored);
		labeledRestored.add(restored);

		main = new JPanel();
		main.setLayout(new GridLayout(2,3));
		main.add(control);
		main.add(new JPanel());
		main.add(condition);
		main.add(labeledOriginal);
		main.add(labeledDegraded);
		main.add(labeledRestored);

		this.add(main);
	}

	public static void main(String[] args) {
		ImageRestoring frame = new ImageRestoring();
		frame.setVisible(true);
	}

	private void initialize() {
		original.defaultInitialize();
	}

	private void reset() {
		original.regenerateSpins(true);
		degraded.regenerateSpins(true);
		restored.regenerateSpins(true);
	}

	private void recreate() {
		original.regenerateSpins();
	}

	private void degrade() {
		revProb = Float.parseFloat(revProbField.getText());
		degraded.degrade(original.getSpins(), revProb);
	}

	private void restore() {
		double p;
		double bp = getBetaP();
		double bm = 1.0/Double.parseDouble(tempM.getText());
		double J = Double.parseDouble(exch.getText());
		double error = Double.parseDouble(err.getText());
		p = restored.restore(degraded.getSpins(), bp, bm, J, error);
		prob.setText(Double.toString(p));
	}

	private double getBetaP() {
		return .5 * Math.log((1.0-revProb)/revProb);
	}

}
