package com.yojiokisoft.japanesecalc;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Calc implements Context {
	private double A; // 電卓はメモリＡを持ちます
	private double B; // 電卓はメモリＢを持ちます
	private double M; // 電卓はメモリＭを持ちます
	private Operation op; // 電卓は演算子を持ちます
	protected AbstractDisplay disp; // 電卓はディスプレイを持ちます
	protected State state; // 電卓の状態を表すクラス
	protected android.content.Context parent; // Toast表示用のcontext

	public Calc() {
		A = 0d;
		B = 0d;
		M = 0d;
		op = null;
		changeState(NumberAState.getInstance());
	}

	public void setDisplay(ViewGroup viewGroup, android.content.Context context) {
		disp = new GraphicDisplay(viewGroup, context);
	}

	public void setDisplay(TextView txt, TextView txtMemory, TextView txtError) {
		disp = new StringDisplay(txt, txtMemory, txtError);
	}

	public void setDisplay(TextView txt, TextView txtMemory, TextView txtError, android.content.Context parent) {
		this.disp = new StringDisplay(txt, txtMemory, txtError);
		this.parent = parent;
	}

	public void onButtonNumber(Number num) {
		state.onInputNumber(this, num);
	}

	public void onButtonOp(Operation op) {
		state.onInputOperation(this, op);
	}

	public void onButtonBackspace() {
		state.onInputBackspace(this);
	}

	public void onButtonClear() {
		state.onInputClear(this);
	}

	public void onButtonAllClear() {
		state.onInputAllClear(this);
	}

	public void onButtonEquale() {
		state.onInputEquale(this);
	}

	public void onButtonPercent() {
		state.onInputPercent(this);
	}

	public void onButtonMemoryPlus() {
		state.onInputMemoryPlus(this);
	}

	public void onButtonMemoryMinus() {
		state.onInputMemoryMinus(this);
	}

	public void onButtonClearMemory() {
		state.onInputClearMemory(this);
	}

	public void onButtonReturnMemory() {
		state.onInputReturnMemory(this);
	}

	@Override
	public void changeState(State newState) {
		state = newState;
	}

	@Override
	public double doOperation() throws CalcException {
		double result = op.eval(A, B);
		// Double の場合ゼロ割でエラーが発生しないので注意
		if (Double.isInfinite(result) || Double.isNaN(result)) {
			throw new CalcException();
		}
		showDisplay(result);
		// 演算結果がディスプレイからはみ出さないか
		if (disp.isOverflow(result)) {
			throw new CalcException();
		}
		return result;
	}

	@Override
	public double doPercent() throws CalcException {
		double result;
		if (op == Operation.PLUS) {
			result = A + (A * B / 100);
		} else if (op == Operation.MINUS) {
			result = A - (A * B / 100);
		} else if (op == Operation.TIMES) {
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
		if (disp.isOverflow(result)) {
			throw new CalcException();
		}
		return result;
	}

	@Override
	public void showDisplay() {
		disp.showDisplay(true);
	}

	@Override
	public void showDisplay(double d) {
		disp.setNumber(d);
		disp.showDisplay(true);
	}

	@Override
	public void addDisplayNumber(Number num) {
		if (num == Number.ZERO || num == Number.DOUBLE_ZERO) {
			if (disp.displayChar.size() == 0 && !disp.commaMode) {
				disp.showDisplay(false);
				return;
			}
		}
		if (num == Number.COMMA && !disp.commaMode && disp.displayChar.size() == 0) {
			disp.onInputNumber(Number.ZERO);
		}
		disp.onInputNumber(num);
		disp.showDisplay(false);
	}

	@Override
	public void saveDisplayNumberToA() {
		A = disp.getNumber();
	}

	@Override
	public void saveDisplayNumberToB() {
		B = disp.getNumber();
	}

	@Override
	public void backspace() {
		disp.onInputBackspace();
		disp.showDisplay(false);
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
		return op;
	}

	@Override
	public void setOp(Operation newOp) {
		op = newOp;
	}

	@Override
	public void clearDisplay() {
		disp.clear();
		disp.showDisplay(false);
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
		if (parent != null) {
			Toast.makeText(parent, "Error", Toast.LENGTH_LONG).show();
		}
		disp.setError();
	}

	@Override
	public void clearError() {
		disp.clearError();
	}

	@Override
	public void changeSign() {
		if (disp.getNumber() != 0d) {
			disp.minus = !disp.minus;
			disp.showDisplay(false);
		}
	}

	@Override
	public void memoryPlus() {
		M += disp.getNumber();
		disp.setMemory(M);
	}

	@Override
	public void memoryMinus() {
		M -= disp.getNumber();
		disp.setMemory(M);
	}

	@Override
	public void clearMemory() {
		M = 0;
		disp.setMemory(M);
	}

	@Override
	public void returnMemory() {
		disp.setNumber(M);
		disp.showDisplay(true);
	}
}
