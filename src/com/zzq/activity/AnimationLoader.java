package com.zzq.activity;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
public class AnimationLoader {
	public static Animation alphain;
	public static Animation alphaout;
	public static Animation close;
	public static Animation inAnim;
	public static Animation leftin;
	public static Animation leftout;
	public static Animation open;
	public static Animation outAnim;
	public static Animation rightin;
	public static Animation rightout;

	public static void load(Context paramContext) {
		inAnim = AnimationUtils.loadAnimation(paramContext, R.anim.wave_scale);
		outAnim = AnimationUtils.loadAnimation(paramContext,
				R.anim.wave_scale_out);
		leftout = AnimationUtils.loadAnimation(paramContext,
				R.anim.step_leftout);
		leftin = AnimationUtils.loadAnimation(paramContext, R.anim.step_leftin);
		rightin = AnimationUtils.loadAnimation(paramContext,
				R.anim.step_rightin);
		rightout = AnimationUtils.loadAnimation(paramContext,
				R.anim.step_rightout);
		open = AnimationUtils.loadAnimation(paramContext, R.anim.open);
		close = AnimationUtils.loadAnimation(paramContext, R.anim.close);
		alphain = new AlphaAnimation(0.0F, 1.0F);
		alphain.setDuration(300L);
		alphain.setFillAfter(true);
		alphaout = new AlphaAnimation(1.0F, 0.0F);
		alphaout.setDuration(300L);
		alphaout.setFillAfter(true);
	}
}

/*
 * Location: D:\fanbianyi\xin.jar Qualified Name:
 * com.hj.nce.utils.AnimationLoader JD-Core Version: 0.6.0
 */