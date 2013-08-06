package com.yojiokisoft.japanesecalc;

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
