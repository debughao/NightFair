package com.nightfair.mobille.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class ReboundScrollView extends ScrollView {
	private View inner;
	private float y;
	private Rect normal = new Rect();
	private boolean animationFinish = true;

	public ReboundScrollView(Context context) {
		super(context);
	}

	public ReboundScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onFinishInflate() {
		if (getChildCount() > 0)
			this.inner = getChildAt(0);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (this.inner == null) {
			return super.onTouchEvent(ev);
		}
		commOnTouchEvent(ev);

		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		if (this.animationFinish) {
			int action = ev.getAction();
			switch (action) {
			case 0:
				this.y = ev.getY();
				super.onTouchEvent(ev);
				break;
			case 1:
				this.y = 0.0F;
				if (isNeedAnimation()) {
					animation();
				}
				super.onTouchEvent(ev);
				break;
			case 2:
				float preY = this.y == 0.0F ? ev.getY() : this.y;
				float nowY = ev.getY();
				int deltaY = (int) (preY - nowY);

				this.y = nowY;

				if (isNeedMove()) {
					if (this.normal.isEmpty()) {
						this.normal.set(this.inner.getLeft(),
								this.inner.getTop(), this.inner.getRight(),
								this.inner.getBottom());
					}

					this.inner.layout(this.inner.getLeft(), this.inner.getTop()
							- deltaY / 2, this.inner.getRight(),
							this.inner.getBottom() - deltaY / 2);
				} else {
					super.onTouchEvent(ev);
				}
				break;
			}
		}
	}

	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(0.0F, 0.0F, 0.0F,
				this.normal.top - this.inner.getTop());
		ta.setDuration(200L);
		ta.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
				ReboundScrollView.this.animationFinish = false;
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				ReboundScrollView.this.inner.clearAnimation();

				ReboundScrollView.this.inner.layout(
						ReboundScrollView.this.normal.left,
						ReboundScrollView.this.normal.top,
						ReboundScrollView.this.normal.right,
						ReboundScrollView.this.normal.bottom);
				ReboundScrollView.this.normal.setEmpty();
				ReboundScrollView.this.animationFinish = true;
			}
		});
		this.inner.startAnimation(ta);
	}

	public boolean isNeedAnimation() {
		return !this.normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = this.inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		if ((scrollY == 0) || (scrollY == offset)) {
			return true;
		}
		return false;
	}
}
