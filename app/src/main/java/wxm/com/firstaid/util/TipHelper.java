package wxm.com.firstaid.util;

import java.util.Random;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;

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
}
