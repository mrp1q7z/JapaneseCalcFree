package com.yojiokisoft.japanesecalc;

import java.math.BigDecimal;

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
