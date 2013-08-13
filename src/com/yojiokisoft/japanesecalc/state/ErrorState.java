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
 * エラー状態
 */
public class ErrorState implements State {
	private static ErrorState singleton = new ErrorState();

	// コンストラクタはプライベート
	private ErrorState() {
	}

	// 唯一のインスタンスを得る
	public static ErrorState getInctance() {
		return singleton;
	}

	@Override
	public void onInputNumber(CalcContext context, Number num) {
	}

	@Override
	public void onInputOperation(CalcContext context, Operation op) {
	}

	@Override
	public void onInputEquale(CalcContext context) {
	}

	@Override
	public void onInputBackspace(CalcContext context) {
	}

	@Override
	public void onInputClear(CalcContext context) {
	}

	@Override
	public void onInputAllClear(CalcContext context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();
		context.clearError();
		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputPercent(CalcContext context) {
	}

	@Override
	public void onInputMemoryPlus(CalcContext context) {
	}

	@Override
	public void onInputMemoryMinus(CalcContext context) {
	}

	@Override
	public void onInputClearMemory(CalcContext context) {
	}

	@Override
	public void onInputReturnMemory(CalcContext context) {
	}
}
