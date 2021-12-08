import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * イジングスピンを格納したパネルを表現するクラス
 *
 */
public class IsingSpin extends JPanel implements MouseListener {
/*
 * **********************************************************************
 * 									fields
 * **********************************************************************
 */
	/**
	 * イジングスピンを表す真理値。
	 * up-spinをtrue, down-spinをfalseで定義した。
	 */
	private boolean spin = true;

	/**
	 * スピン反転の固定を表すフラグです。
	 */
	private boolean lockFlag = false;

	/**
	 * 原画像スピン系を設定するための描画方法を格納する。
	 */
	private DrawType drawType = DrawType.DOT;

/*
 * **********************************************************************
 * 								constructors
 * **********************************************************************
 */
	/**
	 * スピンをupで初期化するコンストラクタ。
	 */
	public IsingSpin() {
		this(true);
		setBorder(new LineBorder(Color.GRAY));
	}

	/**
	 * 引数で指定されたスピンで初期化するコンストラクタ。
	 * @param spin
	 */
	public IsingSpin(boolean spin) {
		setSpin(spin);
		setBorder(new LineBorder(Color.GRAY));
		addMouseListener(this);
	}

/*
 * **********************************************************************
 *							 setters and getters
 * **********************************************************************
 */
	/**
	 * スピンを返す。
	 * @return スピンの状態
	 */
	public boolean isSpin() {
		return spin;
	}

	/**
	 * 引数で与えられた倍精度実数値によってスピンを設定する。
	 * @param x その正負によって上下を決める
	 */
	public void setSpin(double x) {
		setSpin(toIsingSpin(x));
	}

	/**
	 * 引数で指定したスピンに設定する。
	 * @param spin
	 */
	public void setSpin(boolean spin) {
		this.spin = spin;
		setBgColor();
	}

	/**
	 * 描画タイプを指定する。
	 * @param t
	 */
	public void setDrawType(DrawType t) {
		drawType = t;
	}

/*
 * **********************************************************************
 * 								methods
 * **********************************************************************
 */
	/**
	 * パネルの背景を設定する。
	 * upであれば白、downであれば黒。
	 */
	private void setBgColor() {
		if (spin == true)
			this.setBackground(Color.WHITE);
		else
			this.setBackground(Color.BLACK);
	}

	/**
	 * スピンを反転する。
	 */
	public void reverse() {
		setSpin(!spin);
	}

	/**
	 * スピンの値を倍精度実数型で返す。
	 * @return upならば1.0, downならば-1.0を返す
	 */
	public double toDouble() {
		return spin ? 1.0 : -1.0;
	}

	/**
	 * 指定された倍精度実数値によってスピンを設定する。
	 * @param x 正ならup, 負ならdown
	 * @return スピンのboolean表現
	 * @throws IllegalArgumentException 引数が0.0の場合にスローされる
	 */
	private boolean toIsingSpin(double x) throws IllegalArgumentException {
		x = Math.signum(x);

		boolean tmp;
		if (x > 0.0)
			tmp = true;
		else if (x < 0.0)
			tmp = false;
		else
			throw new IllegalArgumentException();

		return tmp;
	}

	/**
	 * 外部操作によるスピン反転を不可にする。
	 */
	public void lock() {
		lockFlag = true;
	}

/*
 * **********************************************************************
 * 								methods
 * **********************************************************************
 */
	public void mouseClicked(MouseEvent e) {
		if (lockFlag == false && drawType == DrawType.DOT)
			reverse();
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {
		if (lockFlag == false) {
			if (drawType == DrawType.DRAW)
				setSpin(false);
			else if (drawType == DrawType.ERASE)
				setSpin(true);
		}
	}
	public void mouseExited(MouseEvent e) {}
}
