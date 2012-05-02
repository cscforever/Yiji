package com.zzq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class TestActivity extends Activity implements OnGestureListener {
    /** Called when the activity is first created. */
	
	private ViewFlipper flipper;//ViewFlipperʵ��
	private GestureDetector detector;//��������ʵ��	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);
        
        detector = new GestureDetector(this);//��ʼ������̽��
        flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);//���ViewFlipperʵ��
         
        flipper.addView(addTextView("step 1"));
        flipper.addView(addTextView("step 2"));
        
        
    }
    private View addTextView(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setGravity(1);
		return tv;
	}    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}   
    
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {//����Ǵ������󻬶�
						//ע��flipper�Ľ��Ч��
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.layout.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.layout.left_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {//����Ǵ������һ���
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.layout.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.layout.right_out));
			this.flipper.showPrevious();
			return true;
		}
		return false;
	}
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}    
}