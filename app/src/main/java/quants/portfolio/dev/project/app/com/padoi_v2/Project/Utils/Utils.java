package quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;

public class Utils {
    public static final float LOADING_ROTATION =-360*2f ;
    //public static final long LOADING_DURATION =getResources().getInteger(R.integer.loading_duration);;
    public static void debugLog(Context context,String TAG, String s, boolean toast) {
        Log.d(TAG, s);
        if(toast) Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        Log.e(TAG,s);
    }
    /**
     * Lets load the video thumbnail.
     */
    public static void prepareVideo(Context context, final VideoView videoView, Event event) {
        try {
            final MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            Uri uri = Uri.parse(event.getVideo_url());
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();
                }
            });
            videoView.requestFocus();
            videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        videoView.start();
                    }else {
                        videoView.stopPlayback();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
