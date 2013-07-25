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
	public void onInputNumber(Context context, Number num) {
		context.clearDisplay();
		context.addDisplayNumber(num);

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputOperation(Context context, Operation op) {
		context.saveDisplayNumberToA();
		context.setOp(op);

		context.changeState(OperationState.getInstance());
	}

	@Override
	public void onInputEquale(Context context) {
		// 何もしない
	}

	@Override
	public void onInputClear(Context context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputAllClear(Context context) {
		context.clearA();
		context.clearB();
		context.clearDisplay();

		context.changeState(NumberAState.getInstance());
	}

	@Override
	public void onInputPercent(Context context) {
		// 何もしない
	}

	@Override
	public void onInputMemoryPlus(Context context) {
		context.memoryPlus();
	}

	@Override
	public void onInputMemoryMinus(Context context) {
		context.memoryMinus();
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
