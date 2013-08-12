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

/**
 * 数値Bの入力状態
 */
public class NumberBState implements State {
	private static NumberBState singleton = new NumberBState();

	// コンストラクタはプライベート
	private NumberBState() {
	}

	// 唯一のインスタンスを得る
	public static NumberBState getInstance() {
		return singleton;
	}

	@Override
	public void onInputNumber(CalcContext context, Number num) {
		context.addDisplayNumber(num);
	}

	@Override
	public void onInputOperation(CalcContext context, Operation op) {
		try {
			context.saveDisplayNumberToB();
			context.doOperation();
			context.setOp(op);
			context.saveDisplayNumberToA();
			context.changeState(OperationState.getInstance());
		} catch (CalcException e) {
			context.setError();
			context.changeState(ErrorState.getInctance());
		}
	}

	@Override
	public void onInputEquale(CalcContext context) {
		try {
			context.saveDisplayNumberToB();
			context.doOperation();
			context.changeState(ResultState.getInstance());
		} catch (CalcException e) {
			context.setError();
			context.changeState(ErrorState.getInctance());
		}
	}

	@Override
	public void onInputBackspace(CalcContext context) {
		context.backspace();
	}

	@Override
	public void onInputClear(CalcContext context) {
		context.clearB();
		context.clearDisplay();
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
		try {
			context.saveDisplayNumberToB();
			context.doPercent();
			context.changeState(ResultState.getInstance());
		} catch (CalcException e) {
			context.setError();
			context.changeState(ErrorState.getInctance());
		}
	}

	@Override
	public void onInputMemoryPlus(CalcContext context) {
		try {
			context.saveDisplayNumberToB();
			context.doOperation();
			context.memoryPlus();
			context.changeState(ResultState.getInstance());
		} catch (CalcException e) {
			context.setError();
			context.changeState(ErrorState.getInctance());
		}
	}

	@Override
	public void onInputMemoryMinus(CalcContext context) {
		try {
			context.saveDisplayNumberToB();
			context.doOperation();
			context.memoryMinus();
			context.changeState(ResultState.getInstance());
		} catch (CalcException e) {
			context.setError();
			context.changeState(ErrorState.getInctance());
		}
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
