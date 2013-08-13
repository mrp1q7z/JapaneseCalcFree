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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.yojiokisoft.japanesecalc.state.ErrorState;
import com.yojiokisoft.japanesecalc.state.NumberAState;
import com.yojiokisoft.japanesecalc.state.State;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 電卓の機能実装
 */
public class Calc implements CalcContext {
	private boolean mIsError; // エラー状態かどうか
	private BigDecimal A; // 電卓はメモリＡを持ちます
	private BigDecimal B; // 電卓はメモリＢを持ちます
	private BigDecimal M; // 電卓はメモリＭを持ちます
	private Operation mOp; // 電卓は演算子を持ちます
	protected AbstractDisplay mDisp; // 電卓はディスプレイを持ちます
	protected State mState; // 電卓の状態を表すクラス
	protected Context mContext; // Toast表示用のcontext

	public String getInstanceState() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.valueOf(A)); // 0
		sb.append("," + String.valueOf(B)); // 1
		sb.append("," + String.valueOf(M)); // 2
		sb.append("," + mDisp.getNumber()); // 3
		sb.append("," + ((mOp == null) ? "null" : mOp.toString())); // 4
		sb.append("," + ((mState == null) ? "null" : mState.getClass().getName())); // 5
		sb.append("," + mIsError); // 6

		return sb.toString();
	}

	public void restoreInstanceState(String state) {
		String[] stateArray = state.split(",");
		A = new BigDecimal(stateArray[0]);
		B = new BigDecimal(stateArray[1]);
		M = new BigDecimal(stateArray[2]);
		mDisp.setNumber(new BigDecimal(stateArray[3]));
		mDisp.showDisplay(false);
		mDisp.setMemory(M.doubleValue());

		if (!stateArray[4].equals("null")) {
			if (stateArray[4].equals("PLUS")) {
				mOp = Operation.PLUS;
			} else if (stateArray[4].equals("MINUS")) {
				mOp = Operation.MINUS;
			} else if (stateArray[4].equals("TIMES")) {
				mOp = Operation.TIMES;
			} else if (stateArray[4].equals("DIVIDE")) {
				mOp = Operation.DIVIDE;
			}
		}

		if (!stateArray[5].equals("null")) {
			try {
				Class clazz = Class.forName(stateArray[5]);
				if (clazz != null) {
					Method factoryMethod = clazz.getDeclaredMethod("getInstance");
					mState = (State) factoryMethod.invoke(null, null);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		mIsError = (stateArray[6].equals("true") ? true : false);
		if (mIsError) {
			mState = ErrorState.getInctance();
			setError();
		}

		// test
		String s = getInstanceState();
		if (!s.equals(state)) {
			throw new RuntimeException("状態が復元できませんでした");
		}
	}

	public Calc() {
		A = new BigDecimal("0");
		B = new BigDecimal("0");
		M = new BigDecimal("0");
		mOp = null;
		changeState(NumberAState.getInstance());
	}

	public void setDisplay(ViewGroup viewGroup, Context context, int orientation) {
		if (mDisp != null) {
			mDisp = null;
		}
		mDisp = new GraphicDisplay(viewGroup, context, orientation);
		mContext = context;
	}

	public void onButtonNumber(Number num) {
		mState.onInputNumber(this, num);
	}

	public void onButtonOp(Operation op) {
		mState.onInputOperation(this, op);
	}

	public void onButtonBackspace() {
		mState.onInputBackspace(this);
	}

	public void onButtonClear() {
		mState.onInputClear(this);
	}

	public void onButtonAllClear() {
		mState.onInputAllClear(this);
	}

	public void onButtonEquale() {
		mState.onInputEquale(this);
	}

	public void onButtonPercent() {
		mState.onInputPercent(this);
	}

	public void onButtonMemoryPlus() {
		mState.onInputMemoryPlus(this);
	}

	public void onButtonMemoryMinus() {
		mState.onInputMemoryMinus(this);
	}

	public void onButtonClearMemory() {
		mState.onInputClearMemory(this);
	}

	public void onButtonReturnMemory() {
		mState.onInputReturnMemory(this);
	}

	@Override
	public void changeState(State newState) {
		mState = newState;
	}

	@Override
	public BigDecimal doOperation() throws CalcException {
		BigDecimal result;
		try {
			result = mOp.eval(A, B);
		} catch (ArithmeticException e) {
			throw new CalcException();
		}
		showDisplay(result);
		// 演算結果がディスプレイからはみ出さないか
		if (mDisp.isOverflow(result)) {
			throw new CalcException();
		}
		return result;
	}

	@Override
	public BigDecimal doPercent() throws CalcException {
		BigDecimal hyaku = new BigDecimal("100");
		BigDecimal result;
		try {
			if (mOp == Operation.PLUS) {
				result = A.add((A.multiply(B).divide(hyaku)));
			} else if (mOp == Operation.MINUS) {
				result = A.subtract((A.multiply(B).divide(hyaku)));
			} else if (mOp == Operation.TIMES) {
				result = A.multiply(B).divide(hyaku);
			} else {
				result = A.divide(B).multiply(hyaku);
			}
		} catch (ArithmeticException e) {
			throw new CalcException();
		}
		showDisplay(result);
		// 演算結果がディスプレイからはみ出さないか
		if (mDisp.isOverflow(result)) {
			throw new CalcException();
		}
		return result;
	}

	@Override
	public void showDisplay() {
		mDisp.showDisplay(true);
	}

	@Override
	public void showDisplay(BigDecimal d) {
		mDisp.setNumber(d);
		mDisp.showDisplay(true);
	}

	@Override
	public void addDisplayNumber(Number num) {
		if (num == Number.ZERO || num == Number.DOUBLE_ZERO) {
			if (mDisp.mDisplayChar.size() == 0 && !mDisp.mCommaMode) {
				mDisp.showDisplay(false);
				return;
			}
		}
		if (num == Number.COMMA && !mDisp.mCommaMode && mDisp.mDisplayChar.size() == 0) {
			mDisp.onInputNumber(Number.ZERO);
		}
		mDisp.onInputNumber(num);
		mDisp.showDisplay(false);
	}

	@Override
	public void saveDisplayNumberToA() {
		A = mDisp.getNumber();
	}

	@Override
	public void saveDisplayNumberToB() {
		B = mDisp.getNumber();
	}

	@Override
	public void backspace() {
		mDisp.onInputBackspace();
		mDisp.showDisplay(false);
	}

	@Override
	public void clearA() {
		A = new BigDecimal("0");
	}

	@Override
	public void clearB() {
		B = new BigDecimal("0");
	}

	@Override
	public Operation getOp() {
		return mOp;
	}

	@Override
	public void setOp(Operation newOp) {
		mOp = newOp;
	}

	@Override
	public void clearDisplay() {
		mDisp.clear();
		mDisp.showDisplay(false);
	}

	@Override
	public void copyAtoB() {
		B = A;
	}

	@Override
	public BigDecimal getA() {
		return A;
	}

	@Override
	public BigDecimal getB() {
		return B;
	}

	@Override
	public void setError() {
		if (mContext != null) {
			Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
		}
		mDisp.setError();
		mIsError = true;
	}

	@Override
	public void clearError() {
		mDisp.clearError();
		mIsError = false;
	}

	@Override
	public void changeSign() {
		if (!mDisp.getNumber().equals(new BigDecimal("0"))) {
			mDisp.mMinus = !mDisp.mMinus;
			mDisp.showDisplay(false);
		}
	}

	@Override
	public void memoryPlus() {
		M = M.add(mDisp.getNumber());
		mDisp.setMemory(M.doubleValue());
	}

	@Override
	public void memoryMinus() {
		M = M.subtract(mDisp.getNumber());
		mDisp.setMemory(M.doubleValue());
	}

	@Override
	public void clearMemory() {
		if (M != null) {
			M = null;
		}
		M = new BigDecimal("0");
		mDisp.setMemory(M.doubleValue());
	}

	@Override
	public void returnMemory() {
		mDisp.setNumber(M);
		mDisp.showDisplay(true);
	}
}
