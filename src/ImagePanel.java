import java.awt.*;
import javax.swing.*;
import java.util.Random;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * イジングスピンパネルを一括管理するためのパネル。
 * このパネルによって画像を表現する。
 * 画像の劣化や修復はこのクラスによって行う。
 */
public class ImagePanel extends JPanel {
/*
 * **********************************************************************
 * 									fields
 * **********************************************************************
 */
	/**
	 * 横または縦のピクセル数
	 * 推奨値はnum * unitLength = 300
	 */
	private int num = 30;

	/**
	 * ピクセルの幅または高さ
	 * 推奨値はnum * unitLength = 300
	 */
	private int unitLength = 10;

	/**
	 * 乱数発生インスタンス
	 */
	private Random rand = new Random(0);

	/**
	 * 画像を表すイジングスピンの配列
	 */
	private IsingSpin[] spins;

	/**
	 * 画像を格納するパネル
	 */
	private JPanel systemPanel;

/*
 * **********************************************************************
 * 								constructors
 * **********************************************************************
 */

	/**
	 * 指定されたスピンですべてのスピンを初期化する。
	 * @param ud
	 */
	public ImagePanel(boolean ud) {
		setSpins(generateSpins(ud));
		addSpinsToPanel();
	}

	/**
	 * 一様分布によって画像を生成する。
	 */
	public ImagePanel() {
		setSpins(generateSpins());
		addSpinsToPanel();
	}

	/**
	 * 指定されたスピン配列で画像を初期化する。
	 * @param spins
	 */
	public ImagePanel(IsingSpin[] spins) {
		setSpins(spins);
		addSpinsToPanel();
	}

/*
 * **********************************************************************
 *							 setters and getters
 * **********************************************************************
 */

	/**
	 * イジングスピン配列を返す。
	 * @return
	 */
	public IsingSpin[] getSpins() {
		return spins;
	}

	/**
	 * イジングスピン配列を設定する。
	 * @param s
	 */
	public void setSpins(IsingSpin[] s) {
		spins = s;
	}


/*
 * **********************************************************************
 * 								methods
 * **********************************************************************
 */
	/**
	 * 指定されたスピンで初期化された配列を生成して返す。
	 * @param ud
	 * @return スピン配列
	 */
	public IsingSpin[] generateSpins(boolean ud) {
		IsingSpin[] s = new IsingSpin[num*num];
		for (int i = 0; i < num * num; i++) {
			s[i] = new IsingSpin(ud);
			s[i].setPreferredSize(new Dimension(unitLength, unitLength));
		}
		return s;
	}

	/**
	 * 一様分布スピン配列を生成して返す。
	 * @return 一様分布スピン配列
	 */
	private IsingSpin[] generateSpins() {
		IsingSpin[] s = new IsingSpin[num*num];
		for (int i = 0; i < num * num; i++) {
			s[i] = new IsingSpin(rand.nextBoolean());
			s[i].setPreferredSize(new Dimension(unitLength, unitLength));
		}
		return s;
	}

	/**
	 * 指定されたスピンでスピン配列を設定する。
	 * @param ud
	 */
	public void regenerateSpins(boolean ud) {
		for (IsingSpin tmp : spins) {
			tmp.setSpin(ud);
		}
	}

	/**
	 * 一様分布スピンでスピン配列を設定する。
	 */
	public void regenerateSpins() {
		for (IsingSpin tmp : spins) {
			if (rand.nextBoolean())
				tmp.reverse();
		}
	}

	/**
	 * デフォルトの元画像を描画する。
	 * デフォルトは蝶ネクタイを45度反時計回りに回転したもの。
	 */
	public void defaultInitialize() {
		int radius = num / 2 - 2;
		int origin = num * num / 2 - num / 2;
		// 第1象限の描画
		for (int y = 0; y < radius; y++)
			for (int x = 0; x < radius - y; x++)
				spins[origin + x - num * y].reverse();
		// 第3象限の描画
		for (int y = 0; y < radius; y++)
			for (int x = 0; x < radius - y; x++)
				spins[origin - x + num * y].reverse();
	}

	/**
	 * スピンをパネルに追加する。
	 */
	private void addSpinsToPanel() {
		systemPanel = new JPanel();
		systemPanel.setLayout(new GridLayout(num,num));
		for (IsingSpin tmp : spins)
			systemPanel.add(tmp);
		this.add(systemPanel);
	}

	/**
	 * 画像を劣化させる。
	 * スピン配列を指定された確率で反転させる。
	 * @param in 原画像スピン配列
	 * @param p 反転確率
	 */
	public void degrade(IsingSpin[] in, double p) {
			copy(in);
			for (IsingSpin tmp : spins)
				if (p > rand.nextFloat())
					tmp.reverse();

	}

	/**
	 * 指定されたスピン配列をコピーして設定する。
	 * @param s コピー元スピン配列
	 */
	public void copy(IsingSpin[] s) {
		for (int i = 0; i < s.length; i++)
			spins[i].setSpin(s[i].isSpin());
	}

	/**
	 * 手動によるスピン反転をロックする。
	 */
	private void lock() {
		for (IsingSpin tmp : spins)
			tmp.lock();
	}

	/**
	 * 描画タイプを設定する。
	 * @param t
	 */
	public void setDrawType(DrawType t) {
		for (IsingSpin tmp : spins)
			tmp.setDrawType(t);
	}

	/**
	 * 画像を修復する。
	 * 修復には古典平均場アニーリング反復法を使用する。
	 * @param degr　修復元画像スピン配列
	 * @param bp 西森逆温度
	 * @param bm 修復逆温度
	 * @param J 交換定数
	 * @param err 許容誤差
	 * @return 修復画像スピン配列
	 */
	public double restore(IsingSpin[] degr, double bp, double bm, double J, double err) {
		double prob = 0.0;							// 事後確率
		double deno = 1.0;							// 事後確率の分母
		double tmp = 0.0;							// 一時変数
		double sum = 0.0;							// 一時変数
		double epsilon = 0.0;						// 誤差を評価するための変数
		ArrayList<Integer> list;					// 隣接格子番号を格納するリスト
		Iterator<Integer> itr;						// リストの反復子

		double[] m = new double[degr.length];			// 実数計算を行うためにスピンを倍精度実数型として扱う
		double[] mb = new double[degr.length];			// 反復における直前の結果を格納する
		for (int i = 0; i < m.length; i++) {			// 劣化画像を初期値とする
			m[i] = degr[i].toDouble();
		}

		bm *= J;
		// 反復計算
		do {
			epsilon = 0.0;
			for (int i = 0; i < degr.length; i++) {
				mb[i] = m[i];
				sum = 0.0;

				list = getNNIndexList(i);
				itr = list.iterator();
				while (itr.hasNext())
					sum += m[itr.next()];

				m[i] = tanh(bm * sum + bp * degr[i].toDouble());
				epsilon += m[i] - mb[i];
			}

		} while (abs(epsilon / (double)(num*num)) > err);

		// 実数によって計算されたスピンの値をイジングスピンに変換する
		for (int i = 0; i < m.length; i++)
			try {
				spins[i].setSpin(m[i]);
			} catch (IllegalArgumentException e) {}

		// 事後確率の分子の計算
		prob += bp * innerProd(degr, spins);
		prob += bm * sumNN(spins);
		prob = exp(prob);

		// 事後確率の分母の計算
		for (int i = 0; i < degr.length; i++) {
			tmp += cosh(bp * degr[i].toDouble() + bm);
			tmp += cosh(bp * degr[i].toDouble() - bm);
			tmp *= 2.0;
			list = getNNIndexList(i);
			tmp *= (double)(list.size());
			deno *= tmp;
		}

		prob = deno;

		return prob;
	}

	/**
	 * 2つのイジングスピンの実数表現による積を返す。
	 * @param a
	 * @param b
	 * @return
	 */
	private double prod(IsingSpin a, IsingSpin b) {
		return a.toDouble() * b.toDouble();
	}

	/**
	 * 2つのイジングスピン配列の実数表現による内積を返す。
	 * @param a
	 * @param b
	 * @return
	 */
	private double innerProd(IsingSpin[] a, IsingSpin[] b) {
		double cnt = 0.0;
		for (int i = 0; i < a.length; i++)
			cnt += prod(a[i], b[i]);
		return cnt;
	}

	/**
	 * スピン配列内で、すべてのスピンについてその隣接スピンとの積和を返す。
	 * @param a
	 * @return
	 */
	private double sumNN(IsingSpin[] a) {
		double cnt = 0.0;
		ArrayList<Integer> list;
		Iterator<Integer> itr;
		for (int i = 0; i < a.length; i++) {
			list = getNNIndexList(i);
			itr = list.iterator();
			while (itr.hasNext())
				cnt += prod(a[i], a[itr.next()]);
		}
		return cnt;
	}

	/**
	 * 指定されたインデックスをもつスピンの隣接スピンのインデックスを格納したリストを返す。
	 * @param idx
	 * @return
	 */
	private ArrayList<Integer> getNNIndexList(int idx) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (idx / num < num - 1)
			list.add(idx+num);
		if (idx / num > 0)
			list.add(idx-num);
		if (idx % num < num - 1)
			list.add(idx+1);
		if (idx % num > 0)
			list.add(idx-1);
		return list;
	}
}
