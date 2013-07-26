package com.yojiokisoft.japanesecalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Calc calc = new Calc();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Button btnClear = (Button) findViewById(R.id.clear);
		btnClear.setOnLongClickListener(mClearButtonClicked);

		Button btnAllClear = (Button) findViewById(R.id.allclear);
		btnAllClear.setOnLongClickListener(mAllClearButtonClicked);

		TextView txtDisp = (TextView) findViewById(R.id.display);
		TextView txtMem = (TextView) findViewById(R.id.memoryMark);
		TextView txtErr = (TextView) findViewById(R.id.errorMark);
		calc.setDisplay(txtDisp, txtMem, txtErr, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnLongClickListener mClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			calc.onButtonClear();
			return false;
		}
	};

	private OnLongClickListener mAllClearButtonClicked = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			calc.onButtonClearMemory();
			calc.onButtonAllClear();
			return false;
		}
	};

	public void onClickButton(View view) {
		switch (view.getId()) {
		case R.id.zero:
			calc.onButtonNumber(Number.ZERO);
			break;
		case R.id.doublezero:
			calc.onButtonNumber(Number.DOUBLE_ZERO);
			break;
		case R.id.one:
			calc.onButtonNumber(Number.ONE);
			break;
		case R.id.two:
			calc.onButtonNumber(Number.TWO);
			break;
		case R.id.three:
			calc.onButtonNumber(Number.TREE);
			break;
		case R.id.four:
			calc.onButtonNumber(Number.FORE);
			break;
		case R.id.five:
			calc.onButtonNumber(Number.FIVE);
			break;
		case R.id.six:
			calc.onButtonNumber(Number.SIX);
			break;
		case R.id.seven:
			calc.onButtonNumber(Number.SEVEN);
			break;
		case R.id.eight:
			calc.onButtonNumber(Number.EIGHT);
			break;
		case R.id.nine:
			calc.onButtonNumber(Number.NINE);
			break;
		case R.id.plus:
			calc.onButtonOp(Operation.PLUS);
			break;
		case R.id.minus:
			calc.onButtonOp(Operation.MINUS);
			break;
		case R.id.times:
			calc.onButtonOp(Operation.TIMES);
			break;
		case R.id.divide:
			calc.onButtonOp(Operation.DIVIDE);
			break;
		case R.id.comma:
			calc.onButtonNumber(Number.COMMA);
			break;
		case R.id.allclear:
			calc.onButtonAllClear();
			break;
		case R.id.clear:
			calc.onButtonBackspace();
			break;
		case R.id.equal:
			calc.onButtonEquale();
			break;
		case R.id.sign:
			calc.changeSign();
			break;
		case R.id.percent:
			calc.onButtonPercent();
			break;
		case R.id.memoryPlus:
			calc.onButtonMemoryPlus();
			break;
		case R.id.memoryMinus:
			calc.onButtonMemoryMinus();
			break;
		case R.id.clearMemory:
			calc.onButtonClearMemory();
			break;
		case R.id.returnMemory:
			calc.onButtonReturnMemory();
			break;
		default:
			break;
		}
	}
}
