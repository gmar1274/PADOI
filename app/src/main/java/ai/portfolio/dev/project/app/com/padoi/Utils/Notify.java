package ai.portfolio.dev.project.app.com.padoi.Utils;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;
import ai.portfolio.dev.project.app.com.padoi.R;

/**
 * Created by gabe on 3/19/2018.
 */

public class Notify {
    private Context context;
    private String msg;
    private PadoiUser user;
  // private Vibrator vibrator;
    private  NotificationCompat.Builder builder;
    private final String CHANNEL_ID=Notify.class.toString();
    public Notify(Context c){
        this.context = c;
    }


    public Notify message(String msg){
        this.msg = msg;
        this.builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg));
        return this;
    }
    public  Notify createNotification(){
        builder = new NotificationCompat.Builder(this.context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
       /* this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            vibrator.vibrate(500);
        }*/
        return this;
    }

    /**
     * Sets title to user
     * @param user
     * @return
     */
    public Notify fromUser(PadoiUser user){
        this.builder.setContentTitle("New Message from "+user.getName());
        return this;
    }

    /**
     * Finalize the notication and display to user.
     *  Set vibration, ringtone and color
     */
    public void sendNotification(){
        this.builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setLights(Color.CYAN, 3000, 3000);


        long[] v = {500,1000};
        builder.setVibrate(v);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(CHANNEL_ID.hashCode(), builder.build());

    }
}
