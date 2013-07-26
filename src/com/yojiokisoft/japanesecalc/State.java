package com.yojiokisoft.japanesecalc;

public interface State {
	/**
	 * 数値ボタン
	 *
	 * @param context
	 * @param num
	 */
	public abstract void onInputNumber(Context context, Number num);

	/**
	 * 四則演算ボタン
	 *
	 * @param context
	 * @param op
	 */
	public abstract void onInputOperation(Context context, Operation op);

	/**
	 * ＝ボタン
	 *
	 * @param context
	 */
	public abstract void onInputEquale(Context context);

	/**
	 * ％ボタン
	 *
	 * @param context
	 */
	public abstract void onInputPercent(Context context);

	/**
	 * M+ボタン
	 *
	 * @param context
	 */
	public abstract void onInputMemoryPlus(Context context);

	/**
	 * M-ボタン
	 *
	 * @param context
	 */
	public abstract void onInputMemoryMinus(Context context);

	/**
	 * CMボタン
	 *
	 * @param context
	 */
	public abstract void onInputClearMemory(Context context);

	/**
	 * RMボタン
	 *
	 * @param context
	 */
	public abstract void onInputReturnMemory(Context context);

	/**
	 * BSボタン
	 *
	 * @param context
	 */
	public abstract void onInputBackspace(Context context);

	/**
	 * クリアボタン
	 *
	 * @param context
	 */
	public abstract void onInputClear(Context context);

	/**
	 * オールクリアボタン
	 *
	 * @param context
	 */
	public abstract void onInputAllClear(Context context);
}
