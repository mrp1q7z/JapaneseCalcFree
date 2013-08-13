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

import com.yojiokisoft.japanesecalc.state.State;

/**
 * 電卓の機能概要
 */
public interface CalcContext {
	// 状態遷移
	public abstract void changeState(State state);
	// 演算実行し結果をディスプレイに表示します
	public abstract BigDecimal doOperation() throws CalcException;
	// パーセント演算を実行し結果をディスプレイに表示します
	public abstract BigDecimal doPercent() throws CalcException;
	// ディスプレイ表示を更新します
	void showDisplay();
	// ディスプレイ表示を引数の値で更新します
	public abstract void showDisplay(BigDecimal d);
	// ディスプレイ表示に数値を追加します
	public abstract void addDisplayNumber(Number num);
	// ディスプレイ表示を変数Aに保存します
	public abstract void saveDisplayNumberToA();
	// ディスプレイ表示を変数Bに保存します
	public abstract void saveDisplayNumberToB();
	// 直前に入力した１文字を取り消します
	public abstract void backspace();
	// 変数Aをクリアします
	public abstract void clearA();
	// 変数Bをクリアします
	public abstract void clearB();
	// 演算子を取得します
	public abstract Operation getOp();
	// 演算子を設定します
	public abstract void setOp(Operation op);
	// ディスプレイをクリアします
	public abstract void clearDisplay();
	// メモリAからBへコピーします
	public abstract void copyAtoB();
	// メモリAを取得します
	public abstract BigDecimal getA();
	// メモリBを取得します
	public abstract BigDecimal getB();
	// エラー表示を設定します
	public abstract void setError();
	// エラー表示を解除します
	public abstract void clearError();
	// ＋・－記号を反転します
	public abstract void changeSign();
	// メモリーへ加算します
	public abstract void memoryPlus();
	// メモリーから減算します
	public abstract void memoryMinus();
	// メモリーをクリアします
	public abstract void clearMemory();
	// メモリーを読み出します
	public abstract void returnMemory();
}
