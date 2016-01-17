package com.dreamteam.custom.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

public class PathAnimations
{

	private static int xOffset = 16;
	private static int yOffset = -13;

	public static Animation getScaleAnimation(float fromX, float toX,
			float fromY, float toY, int durationMillis)
	{
		ScaleAnimation scale = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(durationMillis);
		scale.setFillAfter(true);
		return scale;
	}

	public static void initOffset(Context context)
	{
		// 由布局文件
		xOffset = (int) (10.667 * context.getResources().getDisplayMetrics().density);
		yOffset = -(int) (8.667 * context.getResources().getDisplayMetrics().density);
	}

	public static Animation getRotateAnimation(float fromDegrees,
			float toDegrees, int durationMillis)
	{
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}

	public static void startAnimationsIn(ViewGroup viewgroup, int durationMillis)
	{
		for (int i = 0; i < viewgroup.getChildCount(); i++)
		{
			ImageButton inoutimagebutton = (ImageButton) viewgroup
					.getChildAt(i);
			inoutimagebutton.setVisibility(0);
			inoutimagebutton.setFocusable(true);
			inoutimagebutton.setClickable(true);
			MarginLayoutParams mlp = (MarginLayoutParams) inoutimagebutton
					.getLayoutParams();
			Animation animation = new TranslateAnimation(Animation.ABSOLUTE,
					mlp.rightMargin - xOffset, Animation.RELATIVE_TO_SELF, 0F,
					Animation.ABSOLUTE, yOffset + mlp.bottomMargin,
					Animation.RELATIVE_TO_SELF, 0F);

			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset((i * 100)
					/ (-1 + viewgroup.getChildCount()));
			animation.setInterpolator(new OvershootInterpolator(2F));
			inoutimagebutton.startAnimation(animation);

		}
	}

	public static void startAnimationsOut(ViewGroup viewgroup,
			int durationMillis)
	{
		for (int i = 0; i < viewgroup.getChildCount(); i++)
		{
			final ImageButton inoutimagebutton = (ImageButton) viewgroup
					.getChildAt(i);
			MarginLayoutParams mlp = (MarginLayoutParams) inoutimagebutton
					.getLayoutParams();
			Animation animation = new TranslateAnimation(0F, mlp.rightMargin
					- xOffset, 0F, yOffset + mlp.bottomMargin);

			animation.setFillAfter(true);
			animation.setDuration(durationMillis);
			animation.setStartOffset(((viewgroup.getChildCount() - i) * 100)
					/ (-1 + viewgroup.getChildCount()));// 顺序倒一下比较舒服
			animation.setInterpolator(new AnticipateInterpolator(2F));
			animation.setAnimationListener(new Animation.AnimationListener()
			{
				@Override
				public void onAnimationStart(Animation arg0)
				{
				}

				@Override
				public void onAnimationRepeat(Animation arg0)
				{
				}

				@Override
				public void onAnimationEnd(Animation arg0)
				{
					// TODO Auto-generated method stub
					inoutimagebutton.setVisibility(8);
					inoutimagebutton.setFocusable(false);
					inoutimagebutton.setClickable(false);
				}
			});
			inoutimagebutton.startAnimation(animation);
		}

	}

}