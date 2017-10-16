package wxm.com.firstaid.util;

import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import wxm.com.firstaid.R;

public class TipHelper {

	public static boolean is_long=false;
	// 播放默认铃声
	// 返回Notification id
	public static int flag=Notification.FLAG_AUTO_CANCEL;
	public static int PlaySound(final Context context) {
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification nt = new Notification();



		nt.defaults = Notification.DEFAULT_SOUND;
		nt.flags=flag;
		int soundId = new Random(System.currentTimeMillis())
				.nextInt(Integer.MAX_VALUE);
			mgr.notify(soundId, nt);

		return soundId;
	}

    //获取系统默认铃声的Uri
    private static Uri getSystemDefultRingtoneUri(final Context context) {
        return RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE);
    }
    public static MediaPlayer mMediaPlayer;

    public static void createAlarm(final Context context){
        mMediaPlayer = MediaPlayer.create(context, getSystemDefultRingtoneUri(context));
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void startAlarm() {

        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
    }

    public static void stopAlarm(){
        mMediaPlayer.pause();
    }

}
