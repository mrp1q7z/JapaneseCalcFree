package com.yojiokisoft.japanesecalc;

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
