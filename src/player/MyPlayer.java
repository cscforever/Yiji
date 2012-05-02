package player;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MyPlayer {
	MediaPlayer mediaPlayer = new MediaPlayer();

	public MyPlayer(AssetFileDescriptor url) {
		try {
			mediaPlayer.setDataSource(url.getFileDescriptor(), url
					.getStartOffset(), url.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
			System.out.println("------------------------>playing");
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.stop();
					mediaPlayer.release();
					System.out.println("------------------------>released");
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public MyPlayer(String url) {
		try {
			mediaPlayer.setDataSource(url);

			mediaPlayer.prepare();
			mediaPlayer.start();
			System.out.println("------------------------>playing");
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.stop();
					mediaPlayer.release();
					System.out.println("------------------------>released");
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
