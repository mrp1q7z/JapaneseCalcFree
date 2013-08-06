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
	public void onInputNumber(CalcContext context, Number num) {
		context.clearDisplay();
		context.addDisplayNumber(num);

		context.changeState(NumberBState.getInstance());
	}

	@Override
	public void onInputOperation(CalcContext context, Operation op) {
		context.setOp(op);
	}

	@Override
	public void onInputEquale(CalcContext context) {
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
	public void onInputBackspace(CalcContext context) {
		context.backspace();
	}

	@Override
	public void onInputClear(CalcContext context) {
		context.clearA();
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
		context.showDisplay(context.getA());
		context.changeState(ResultState.getInstance());
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
		context.changeState(NumberBState.getInstance());
	}
}
