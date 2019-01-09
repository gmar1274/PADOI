package quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

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

    public static String getTag(Class aClass) {
        return aClass.getCanonicalName();
    }
    public static void setLoading(Context context, final View view , boolean isLoading) {

        final long duration = context.getResources().getInteger(R.integer.loading_duration);
        if (isLoading) {//start the animation
            view.setVisibility(View.VISIBLE);
            view.animate().setInterpolator(new AnticipateOvershootInterpolator()).setDuration(duration).rotation(Utils.LOADING_ROTATION).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.animate().setListener(this); //It listens for animation's ending and we are passing this to start onAniationEnd method when animation ends, So it works in loop
                    view.setRotation(0);
                    view.animate().setInterpolator(new AnticipateOvershootInterpolator()).setStartDelay(300).setDuration(duration).rotation(Utils.LOADING_ROTATION).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        } else {//stop the animation
            view.animate().cancel();
            view.setVisibility(View.GONE);
        }
    }
}
