package com.yojiokisoft.japanesecalc;

public interface State {
	/**
	 * 数値ボタン
	 *
	 * @param context
	 * @param num
	 */
	public abstract void onInputNumber(CalcContext context, Number num);

	/**
	 * 四則演算ボタン
	 *
	 * @param context
	 * @param op
	 */
	public abstract void onInputOperation(CalcContext context, Operation op);

	/**
	 * ＝ボタン
	 *
	 * @param context
	 */
	public abstract void onInputEquale(CalcContext context);

	/**
	 * ％ボタン
	 *
	 * @param context
	 */
	public abstract void onInputPercent(CalcContext context);

	/**
	 * M+ボタン
	 *
	 * @param context
	 */
	public abstract void onInputMemoryPlus(CalcContext context);

	/**
	 * M-ボタン
	 *
	 * @param context
	 */
	public abstract void onInputMemoryMinus(CalcContext context);

	/**
	 * CMボタン
	 *
	 * @param context
	 */
	public abstract void onInputClearMemory(CalcContext context);

	/**
	 * RMボタン
	 *
	 * @param context
	 */
	public abstract void onInputReturnMemory(CalcContext context);

	/**
	 * BSボタン
	 *
	 * @param context
	 */
	public abstract void onInputBackspace(CalcContext context);

	/**
	 * クリアボタン
	 *
	 * @param context
	 */
	public abstract void onInputClear(CalcContext context);

	/**
	 * オールクリアボタン
	 *
	 * @param context
	 */
	public abstract void onInputAllClear(CalcContext context);
}
