package com.zzq.activity;

import java.util.Vector;
import android.graphics.Bitmap;

public class GifFrame
{	
	/* ����gif������֡������ */
    private Vector frames;
    
    /* ��ǰ���ŵ�֡������ */
    private int index;

    public GifFrame() 
    {
    	frames = new Vector(1);
        index = 0;
    }
    
    /* ���һ֡ */
    public void addImage(Bitmap image) 
    {
    	frames.addElement(image);
    }

    /* ����֡�� */
    public int size() 
    {
        return frames.size();
    }

    /* �õ���ǰ֡��ͼƬ */
    public Bitmap getImage() 
    {
        if (size() == 0) 
        {
            return null;
        } 
        else 
        {
            return (Bitmap) frames.elementAt(index);
        }
    }

    /* ��һ֡ */
    public void nextFrame() 
    {
        if (index + 1 < size()) 
        {
            index++;
        } 
        else 
        {
            index = 0;
        }
    }
    
    /* ����GifFrame */
    public static GifFrame CreateGifImage(byte abyte0[]) 
    {
        try 
        {
        	GifFrame GF = new GifFrame();
        	Bitmap image = null;
            GifDecoder gifdecoder = new GifDecoder(abyte0);
            for (; gifdecoder.moreFrames(); gifdecoder.nextFrame()) 
            {
                try 
                {
                    image = gifdecoder.decodeImage();
                    if (GF != null && image != null) 
                    {
                        GF.addImage(image);
                    }
                    continue;
                }
                catch (Exception e) 
                {
                	e.printStackTrace();
                }
                break;
            }
            gifdecoder.clear();
            gifdecoder = null;
            return GF;
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
            return null;
        }
    } 
}

