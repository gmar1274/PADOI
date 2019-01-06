package quants.portfolio.dev.project.app.com.padoi_v2.Project;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class EventStoriesPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Event> mEvents;
    private MediaPlayer.OnCompletionListener mOnCompleteListner;

    public EventStoriesPagerAdapter(Context context, ArrayList<Event> events, MediaPlayer.OnCompletionListener onCompletionListener){
        this.mContext = context;
        this.mEvents = events;
        this.mOnCompleteListner=onCompletionListener;
    }
    @Override
    public int getCount() {
        return this.mEvents.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int pos){
        Event event = this.mEvents.get(pos);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_viewpager_item, viewGroup, false);
        VideoView videoView = layout.findViewById(R.id.videoView);
        videoView= Utils.prepareVideo(mContext,videoView,event);
        videoView.setOnCompletionListener(mOnCompleteListner);
        final ProgressBar progressBar = layout.findViewById(R.id.progressBar);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.start();
            }
        });
        viewGroup.addView(layout);

        return layout;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
