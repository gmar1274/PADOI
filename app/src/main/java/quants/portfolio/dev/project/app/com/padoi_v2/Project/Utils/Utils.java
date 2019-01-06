package quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils;

import android.content.Context;
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
    public static VideoView prepareVideo(Context context, final VideoView videoView, Event event) {
        try {
            final MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            mediaController.setVisibility(View.GONE);
            videoView.setMediaController(mediaController);
            Uri uri = Uri.parse(event.getVideo_url());
            videoView.setVideoURI(uri);
        }catch (Exception e){
            e.printStackTrace();
        }
        return videoView;
    }
}
