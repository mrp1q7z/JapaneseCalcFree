package com.yojiokisoft.japanesecalc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Calc implements CalcContext {
	private double A; // 電卓はメモリＡを持ちます
	private double B; // 電卓はメモリＢを持ちます
	private double M; // 電卓はメモリＭを持ちます
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

		return sb.toString();
	}

	public void restoreInstanceState(String state) {
		String[] stateArray = state.split(",");
		A = Double.parseDouble(stateArray[0]);
		B = Double.parseDouble(stateArray[1]);
		M = Double.parseDouble(stateArray[2]);
		mDisp.setNumber(Double.parseDouble(stateArray[3]));
		mDisp.showDisplay(false);
		mDisp.setMemory(M);

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

		if (stateArray[5].equals("null")) {
			return;
		}
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

	public Calc() {
		A = 0d;
		B = 0d;
		M = 0d;
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

	public void setDisplay(TextView txt, TextView txtMemory, TextView txtError) {
		mDisp = new StringDisplay(txt, txtMemory, txtError);
	}

	public void setDisplay(TextView txt, TextView txtMemory, TextView txtError, Context parent) {
		this.mDisp = new StringDisplay(txt, txtMemory, txtError);
		this.mContext = parent;
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
	public double doOperation() throws CalcException {
		double result = mOp.eval(A, B);
		// Double の場合ゼロ割でエラーが発生しないので注意
		if (Double.isInfinite(result) || Double.isNaN(result)) {
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
	public double doPercent() throws CalcException {
		double result;
		if (mOp == Operation.PLUS) {
			result = A + (A * B / 100);
		} else if (mOp == Operation.MINUS) {
			result = A - (A * B / 100);
		} else if (mOp == Operation.TIMES) {
			result = A * B / 100;
		} else {
			result = A / B * 100;
		}
		// Double の場合ゼロ割でエラーが発生しないので注意
		if (Double.isInfinite(result) || Double.isNaN(result)) {
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
	public void showDisplay(double d) {
		mDisp.setNumber(d);
		mDisp.showDisplay(true);
	}

	@Override
	public void addDisplayNumber(Number num) {
		if (num == Number.ZERO || num == Number.DOUBLE_ZERO) {
			if (mDisp.displayChar.size() == 0 && !mDisp.commaMode) {
				mDisp.showDisplay(false);
				return;
			}
		}
		if (num == Number.COMMA && !mDisp.commaMode && mDisp.displayChar.size() == 0) {
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
		A = 0d;
	}

	@Override
	public void clearB() {
		B = 0d;
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
	public double getA() {
		return A;
	}

	@Override
	public double getB() {
		return B;
	}

	@Override
	public void setError() {
		if (mContext != null) {
			Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
		}
		mDisp.setError();
	}

	@Override
	public void clearError() {
		mDisp.clearError();
	}

	@Override
	public void changeSign() {
		if (mDisp.getNumber() != 0d) {
			mDisp.minus = !mDisp.minus;
			mDisp.showDisplay(false);
		}
	}

	@Override
	public void memoryPlus() {
		M += mDisp.getNumber();
		mDisp.setMemory(M);
	}

	@Override
	public void memoryMinus() {
		M -= mDisp.getNumber();
		mDisp.setMemory(M);
	}

	@Override
	public void clearMemory() {
		M = 0;
		mDisp.setMemory(M);
	}

	@Override
	public void returnMemory() {
		mDisp.setNumber(M);
		mDisp.showDisplay(true);
	}
}
