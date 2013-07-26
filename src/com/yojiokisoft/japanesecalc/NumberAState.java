package com.yojiokisoft.japanesecalc;

public class NumberAState implements State {

	private static NumberAState singleton = new NumberAState();

	private NumberAState() { // コンストラクタはプライベート
	}

	public static State getInstance() {
		return singleton;
	}

	@Override
	public void onInputNumber(Context context, Number num) {
		context.addDisplayNumber(num);
	}

	@Override
	public void onInputOperation(Context context, Operation op) {
		context.saveDisplayNumberToA();
		context.setOp(op);
		context.changeState(OperationState.getInstance());
	}

	@Override
	public void onInputEquale(Context context) {
		context.saveDisplayNumberToA();
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputBackspace(Context context) {
		context.backspace();
	}

	@Override
	public void onInputClear(Context context) {
		context.clearA();
		context.clearDisplay();
	}

	@Override
	public void onInputAllClear(Context context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();
	}

	@Override
	public void onInputPercent(Context context) {
		context.saveDisplayNumberToA();
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputMemoryPlus(Context context) {
		context.memoryPlus();
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputMemoryMinus(Context context) {
		context.memoryMinus();
		context.changeState(ResultState.getInstance());
	}

	@Override
	public void onInputClearMemory(Context context) {
		context.clearMemory();
	}

	@Override
	public void onInputReturnMemory(Context context) {
		context.returnMemory();
	}
}
