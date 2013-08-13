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
 * 演算結果の表示状態
 */
public class ResultState implements State {
	private static ResultState singleton = new ResultState();

	// コンストラクタはプライベート
	private ResultState() {
	}

	// 唯一のインスタンスを得る
	public static ResultState getInstance() {
		return singleton;
	}

	@Override
	public void onInputNumber(CalcContext context, Number num) {
		context.clearDisplay();
		context.addDisplayNumber(num);

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputOperation(CalcContext context, Operation op) {
		context.saveDisplayNumberToA();
		context.setOp(op);

		context.changeState(OperationState.getInstance());
	}

	@Override
	public void onInputEquale(CalcContext context) {
		context.showDisplay();
	}

	@Override
	public void onInputBackspace(CalcContext context) {
		context.backspace();
	}

	@Override
	public void onInputClear(CalcContext context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputAllClear(CalcContext context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputPercent(CalcContext context) {
		// 何もしない
	}

	@Override
	public void onInputMemoryPlus(CalcContext context) {
		context.memoryPlus();
	}

	@Override
	public void onInputMemoryMinus(CalcContext context) {
		context.memoryMinus();
	}

	@Override
	public void onInputClearMemory(CalcContext context) {
		context.clearMemory();
	}

	@Override
	public void onInputReturnMemory(CalcContext context) {
		context.returnMemory();
	}
}