package com.zzq.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

public class GameView extends View implements Runnable {
	Context mContext = null;

	/* ����GifFrame���� */
	GifFrame mGifFrame = null;

	public GameView(Context context) {
		super(context);

		mContext = context;
		/* ����GIF���� */
		mGifFrame = GifFrame.CreateGifImage(fileConnect(this.getResources()
				.openRawResource(R.drawable.r2)));
		/* �����߳� */
		new Thread(this).start();
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/* ��һ֡ */
		mGifFrame.nextFrame();
		/* �õ���ǰ֡��ͼƬ */
		Matrix matrix = new Matrix();
		float scale = 5.0f;
		matrix.postScale(scale, scale);
		Bitmap bitmap = mGifFrame.getImage();
		Bitmap b = Bitmap.createBitmap(mGifFrame.getImage(), 0, 0, bitmap
				.getWidth(), bitmap.getHeight(), matrix, true);

		/* ���Ƶ�ǰ֡��ͼƬ */
		if (b != null)
			canvas.drawBitmap(b, 10, 10, null);
	}

	int count = 0;

	/**
	 * �̴߳���
	 */
	public void run() {
		while (count < 50) {
			count++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			// ʹ��postInvalidate����ֱ�����߳��и��½���
			postInvalidate();
		}
		Thread.currentThread().interrupt();
	}

	/* ��ȡ�ļ� */
	public byte[] fileConnect(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int ch = 0;
			while ((ch = is.read()) != -1) {
				baos.write(ch);
			}
			byte[] datas = baos.toByteArray();
			baos.close();
			baos = null;
			is.close();
			is = null;
			return datas;
		} catch (Exception e) {
			return null;
		}
	}
}
