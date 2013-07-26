package com.yojiokisoft.japanesecalc;

import java.util.Stack;

public abstract class AbstractDisplay {
	// 数値の桁数
	protected final int DISPLAY_DIGIT = 12;
	// ディスプレイに表示される文字をスタックで保持
	protected final Stack<String> displayChar = new Stack<String>();
	// Trueの場合カンマ表示（小数点入力モード）であることを示す
	protected boolean commaMode;
	// 小数点以下の桁数を保持します
	protected int decimalPlaces;
	// マイナス記号です
	protected boolean minus;

	// ディスプレイ表示を行います
	public abstract void showDisplay(boolean format);

	// 押されたボタンに合わせて内部の値を遷移します
	public abstract void onInputNumber(Number num);

	// バックスペースが押されたときの処理
	public abstract void onInputBackspace();

	// ディスプレイの内容をクリアします
	public abstract void clear();

	// ディスプレイの内容をダブル型で取得します
	public abstract double getNumber();

	// 引数の数字を文字列にしてディスプレイに設定します
	public abstract void setNumber(double d);

	// M(メモリ)の有無をディスプレイに表示します
	public abstract void setMemory(double d);

	// エラーを表示します
	public abstract void setError();

	// エラー表示をクリアします
	public abstract void clearError();

	/**
	 * 引数がディスプレイの表示桁数を超えていないかをチェックします.<br>
	 * @param d チェックしたい値
	 * @return true=表示桁数を超える false=超えない
	 */
	public boolean isOverflow(double d) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<DISPLAY_DIGIT; i++) {
			sb.append("9");
		}
		double max = Double.parseDouble(sb.toString());
		if (d > max) {
			return true;
		}
		return false;
	}

	public String toString() {
		return displayChar.toString();
	}
}
