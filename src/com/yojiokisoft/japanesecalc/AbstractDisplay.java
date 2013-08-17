/*
 * Copyright (C) 2013 YojiokiSoft
 * 
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.yojiokisoft.japanesecalc;

import java.math.BigDecimal;
import java.util.Stack;

/**
 * ディスプレイの抽象クラス
 */
public abstract class AbstractDisplay {
	// 数値の桁数
	public static final int DISPLAY_DIGIT = 12;
	// ディスプレイに表示される文字をスタックで保持
	protected final Stack<String> mDisplayChar = new Stack<String>();
	// Trueの場合カンマ表示（小数点入力モード）であることを示す
	protected boolean mCommaMode;
	// 小数点以下の桁数を保持します
	protected int mDecimalPlaces;
	// マイナス記号です
	protected boolean mMinus;
	// 表示中の値の正確な数値（誤差を防ぐため）
	protected BigDecimal mDisplayNumber;

	// ディスプレイ表示を行います
	public abstract void showDisplay(boolean format);

	// 押されたボタンに合わせて内部の値を遷移します
	public abstract void onInputNumber(Number num);

	// バックスペースが押されたときの処理
	public abstract void onInputBackspace();

	// ディスプレイの内容をクリアします
	public abstract void clear();

	// ディスプレイの内容をダブル型で取得します
	public abstract BigDecimal getNumber();

	// 引数の数字を文字列にしてディスプレイに設定します
	public abstract void setNumber(BigDecimal d);

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
	public boolean isOverflow(BigDecimal d) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < DISPLAY_DIGIT; i++) {
			sb.append("9");
		}
		BigDecimal max = new BigDecimal(sb.toString());
		if (d.compareTo(max) > 0) {
			return true;
		}
		BigDecimal min = (new BigDecimal("0")).subtract(max);
		if (d.compareTo(min) < 0) {
			return true;
		}
		return false;
	}

	public String toString() {
		return mDisplayChar.toString();
	}
}
