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
	public void onInputNumber(Context context, Number num) {
		context.addDisplayNumber(num);
	}

	@Override
	public void onInputOperation(Context context, Operation op) {
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
	public void onInputEquale(Context context) {
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
	public void onInputBackspace(Context context) {
		context.backspace();
	}

	@Override
	public void onInputClear(Context context) {
		context.clearB();
		context.clearDisplay();
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
	public void onInputMemoryPlus(Context context) {
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
	public void onInputMemoryMinus(Context context) {
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
	public void onInputClearMemory(Context context) {
		context.clearMemory();
	}

	@Override
	public void onInputReturnMemory(Context context) {
		context.returnMemory();
	}
}
