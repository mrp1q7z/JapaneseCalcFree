package com.yojiokisoft.japanesecalc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
	private IScrollStateListener mListener;

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context) {
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
		if (content.getTop() >= 0) {
			mListener.onScrollMostTop();
		}
		if (content.getTop() < 0) {
			mListener.onScrollFromMostTop();
		}

		if (content.getHeight() <= getHeight()) {
			mListener.onScrollMostBottom();
		}
		if (content.getTop() > getHeight()) {
			mListener.onScrollFromMostBottom();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mListener == null) {
			return;
		}

		if (t == 0) {
			mListener.onScrollMostTop();
		} else if (oldt == 0) {
			mListener.onScrollFromMostTop();
		}

		int mostBottomT = this.getChildAt(0).getHeight() - getHeight();
		if (t >= mostBottomT) {
			mListener.onScrollMostBottom();
		}
		if (oldt >= mostBottomT && t < mostBottomT) {
			mListener.onScrollFromMostBottom();
		}
	}

	public void setScrollStateListener(IScrollStateListener listener) {
		mListener = listener;
	}

	public interface IScrollStateListener {
		void onScrollMostTop();

		void onScrollFromMostTop();

		void onScrollMostBottom();

		void onScrollFromMostBottom();
	}
}
