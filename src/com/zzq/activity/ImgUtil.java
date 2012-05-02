package com.zzq.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgUtil {
	public static Bitmap lesson;
	public static Bitmap[] numbers;
	public static Bitmap[] titleNumbers;

	public static Bitmap createReflectionImageWithOrigin(Bitmap paramBitmap) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Matrix localMatrix = new Matrix();
		localMatrix.preScale(1.0F, -1.0F);
		Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap, 0, j / 2, i,
				j / 2, localMatrix, false);
		Bitmap localBitmap2 = Bitmap.createBitmap(i, j + j / 2,
				Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap2);
		localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
		Paint localPaint1 = new Paint();
		localCanvas.drawRect(0.0F, j, i, j + 4, localPaint1);
		localCanvas.drawBitmap(localBitmap1, 0.0F, j + 4, null);
		Paint localPaint2 = new Paint();
		// localPaint2.setShader(new LinearGradient(0.0F,
		// paramBitmap.getHeight(),
		// 0.0F, 4 + localBitmap2.getHeight(), 1895825407, 16777215,
		// Shader.TileMode.CLAMP));
		// PorterDuffXfermode localPorterDuffXfermode = new PorterDuffXfermode(
		// PorterDuff.Mode.DST_IN);
		// localPaint2.setXfermode(localPorterDuffXfermode);
		localCanvas.drawRect(0.0F, j, i, 4 + localBitmap2.getHeight(),
				localPaint2);
		return localBitmap2;
	}

	public static Bitmap drawableToBitmap(Drawable paramDrawable) {
		int i = paramDrawable.getIntrinsicWidth();
		int j = paramDrawable.getIntrinsicHeight();
		if (paramDrawable.getOpacity() != -1)
			;
		for (Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;; localConfig = Bitmap.Config.RGB_565) {
			Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
			Canvas localCanvas = new Canvas(localBitmap);
			paramDrawable.setBounds(0, 0, i, j);
			paramDrawable.draw(localCanvas);
			return localBitmap;
		}
	}

	public static Bitmap getNumber(int paramInt) {
		return numbers[paramInt];
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap paramBitmap,
			float paramFloat) {
		Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(),
				paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint();
		Rect localRect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap
				.getHeight());
		RectF localRectF = new RectF(localRect);
		localPaint.setAntiAlias(true);
		localCanvas.drawARGB(0, 0, 0, 0);
		localPaint.setColor(-12434878);
		localCanvas.drawRoundRect(localRectF, paramFloat, paramFloat,
				localPaint);
		// localPaint.setXfermode(new
		// PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
		return localBitmap;
	}

	public static Bitmap getShade(Bitmap paramBitmap, int paramInt1,
			int paramInt2, int paramInt3, int paramInt4) {
		Bitmap localBitmap1 = getRoundedCornerBitmap(Bitmap.createBitmap(
				paramInt3 + (paramInt1 + paramBitmap.getWidth()), paramInt4
						+ (paramInt2 + paramBitmap.getHeight()),
				Bitmap.Config.ARGB_8888), 7.0F);
		Bitmap localBitmap2 = getRoundedCornerBitmap(paramBitmap, 7.0F);
		Canvas localCanvas = new Canvas(localBitmap1);
		localCanvas.drawARGB(144, 144, 144, 144);
		localCanvas.drawBitmap(localBitmap2, paramInt1, paramInt2, new Paint());
		localCanvas.save();
		localCanvas.restore();
		return zoomBitmap(localBitmap1, localBitmap2.getWidth(), localBitmap2
				.getHeight());
	}

	

	public static void releaseImg() {
		for (int i = 0;; i++) {
			if (i >= numbers.length) {
				lesson = null;
				return;
			}
			numbers[i] = null;
		}
	}

	public static Bitmap rotateBitmap(Bitmap paramBitmap, int paramInt1,
			int paramInt2, int paramInt3) {
		Matrix localMatrix = new Matrix();
		localMatrix.postRotate(paramInt1, paramInt2, paramInt3);
		return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(),
				paramBitmap.getHeight(), localMatrix, true);
	}

	public static Bitmap zoomBitmap(Bitmap paramBitmap, int paramInt1,
			int paramInt2) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Matrix localMatrix = new Matrix();
		localMatrix.postScale((float)paramInt1 / i, (float)paramInt2 / j);
		return Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
	}

	public Bitmap returnBitMap(String paramString) {
		URL localObject = null;
		Bitmap localBitmap = null;
		try {
			URL localURL = new URL(paramString);
			localObject = localURL;
			HttpURLConnection localHttpURLConnection = (HttpURLConnection) localObject
					.openConnection();
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.connect();
			InputStream localInputStream = localHttpURLConnection
					.getInputStream();
			localBitmap = BitmapFactory.decodeStream(localInputStream);
			localInputStream.close();
			Log.v(" ", localBitmap.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return localBitmap;
		}
	}
}

/*
 * Location: D:\fanbianyi\xin.jar Qualified Name: com.hj.nce.utils.ImgUtil
 * JD-Core Version: 0.6.0
 */