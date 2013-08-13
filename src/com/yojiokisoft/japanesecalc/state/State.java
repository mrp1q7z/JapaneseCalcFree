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

package com.yojiokisoft.japanesecalc.state;

import com.yojiokisoft.japanesecalc.CalcContext;
import com.yojiokisoft.japanesecalc.Number;
import com.yojiokisoft.japanesecalc.Operation;

/**
 * 状態インターフェース
 */
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
