package com.yojiokisoft.japanesecalc;

public class NumberAState implements State {

	private static NumberAState singleton = new NumberAState();

	private NumberAState() { // コンストラクタはプライベート
	}

	public static State getInstance() {
		return singleton;
	}

	@Override
	public void onInputNumber(CalcContext context, Number num) {
		context.addDisplayNumber(num);
	}

	@Override
	public void onInputOperation(CalcContext context, Operation op) {
		context.saveDisplayNumberToA();
		context.setOp(op);
		context.changeState(OperationState.getInstance());
	}

	@Override
	public void onInputEquale(CalcContext context) {
		context.saveDisplayNumberToA();
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputBackspace(CalcContext context) {
		context.backspace();
	}

	@Override
	public void onInputClear(CalcContext context) {
		context.clearA();
		context.clearDisplay();
	}

	@Override
	public void onInputAllClear(CalcContext context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();
	}

	@Override
	public void onInputPercent(CalcContext context) {
		context.saveDisplayNumberToA();
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputMemoryPlus(CalcContext context) {
		context.memoryPlus();
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputMemoryMinus(CalcContext context) {
		context.memoryMinus();
		context.changeState(ResultState.getInstance());
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
