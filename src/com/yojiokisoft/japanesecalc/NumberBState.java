package com.yojiokisoft.japanesecalc;

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
