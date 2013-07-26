package com.yojiokisoft.japanesecalc;

public class OperationState implements State {
	private static OperationState singleton = new OperationState();

	// コンストラクタはプライベート
	private OperationState() {
	}

	// 唯一のインスタンスを得る
	public static OperationState getInstance() {
		return singleton;
	}

	@Override
	public void onInputNumber(Context context, Number num) {
		context.clearDisplay();
		context.addDisplayNumber(num);

		context.changeState(NumberBState.getInstance());
	}

	@Override
	public void onInputOperation(Context context, Operation op) {
		context.setOp(op);
	}

	@Override
	public void onInputEquale(Context context) {
		switch (context.getOp()) {
		case DIVIDE:
		case TIMES:
			try {
				context.copyAtoB();
				context.doOperation();
				context.changeState(ResultState.getInstance());
			} catch (CalcException e) {
				context.setError();
				context.changeState(ErrorState.getInctance());
			}
			break;
		case MINUS:
		case PLUS:
			context.showDisplay(context.getA());
			context.changeState(ResultState.getInstance());
			break;
		default:
			break;
		}
	}

	@Override
	public void onInputBackspace(Context context) {
		context.backspace();
	}

	@Override
	public void onInputClear(Context context) {
		context.clearA();
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
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
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
		context.changeState(NumberBState.getInstance());
	}
}
