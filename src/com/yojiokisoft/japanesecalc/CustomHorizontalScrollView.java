package com.yojiokisoft.japanesecalc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView {
	private IScrollStateListener mListener;

	public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomHorizontalScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			prepare();
		}
	}

	private void prepare() {
		if (mListener == null) {
			return;
		}

		View content = this.getChildAt(0);
		if (content.getLeft() >= 0) {
			mListener.onScrollMostLeft();
		}
		if (content.getLeft() < 0) {
			mListener.onScrollFromMostLeft();
		}

		if (content.getRight() <= getWidth()) {
			mListener.onScrollMostRight();
		}
		if (content.getLeft() > getWidth()) {
			mListener.onScrollFromMostRight();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mListener == null) {
			return;
		}

		if (l == 0) {
			mListener.onScrollMostLeft();
		} else if (oldl == 0) {
			mListener.onScrollFromMostLeft();
		}

		int mostRightL = this.getChildAt(0).getWidth() - getWidth();
		if (l >= mostRightL) {
			mListener.onScrollMostRight();
		}
		if (oldl >= mostRightL && l < mostRightL) {
			mListener.onScrollFromMostRight();
		}
	}

	public void setScrollStateListener(IScrollStateListener listener) {
		mListener = listener;
	}

	public interface IScrollStateListener {
		void onScrollMostLeft();

		void onScrollFromMostLeft();

		void onScrollMostRight();

		void onScrollFromMostRight();
	}
}
