package quants.portfolio.dev.project.app.com.padoi_v2.Project.RVAdapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {
    private String TAG=EventsAdapter.class.getCanonicalName();
    private ArrayList<Event> mDataset;
    private OnItemClickListener listener;
    private Context context;
    //private int last_postition=-1;
    public interface OnItemClickListener {
        void onItemClick(Pair[] pair,Event item);
        void onItemLongClick(Event item);
    }
    public EventsAdapter(Context context, ArrayList<Event> events, OnItemClickListener listener){
        super();
        this.context=context;
        mDataset = events;
        this.listener = listener;
    }
    public ArrayList<Event> getList(){
        return this.mDataset;
    }
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.event_item, viewGroup, false);
        // Return a new holder instance
        EventsViewHolder viewHolder = new EventsViewHolder(contactView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(EventsViewHolder eventsViewHolder, final int pos) {
        Event event = mDataset.get(pos);
        eventsViewHolder.bind(event, listener);
        // Get the data model based on position
        eventsViewHolder.tv.setText(event.getVideo_url());
        eventsViewHolder.animate(pos);
        VideoView videoView = eventsViewHolder.videoView;
        final ProgressBar progressBar = eventsViewHolder.progressBar;
        videoView = Utils.prepareVideo(context,videoView,event);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0,0);//no sound
                progressBar.setVisibility(View.GONE);
                mp.start();
            }
        });

    }
    @Override
    public void onViewDetachedFromWindow(EventsViewHolder viewHolder){
       VideoView videoView = viewHolder.videoView;
       videoView.stopPlayback();
    }
    public void update(ArrayList<Event> list){
        if (mDataset != null) {
            mDataset.clear();
            mDataset.addAll(list);
        }
        else {
            mDataset = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView tv ;
        VideoView videoView;
        CardView cardView;
        ProgressBar progressBar;
        public EventsViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.textView);
            videoView = view.findViewById(R.id.videoView);
            cardView = view.findViewById(R.id.card_view);
            progressBar = view.findViewById(R.id.progressBar);
        }

        /**
         * Interface to communicate with views via overriding onItemClickListener
         * @param item
         * @param listener
         */
        public void bind(final Event item, final OnItemClickListener listener) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(getPair(),item);
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(item);
                    return true;
                }
            });
        }

        private Pair[] getPair() {
            Pair[] p = new Pair[]{Pair.<View, String>create(tv,tv.getTransitionName()), Pair.create(videoView,videoView.getTransitionName())};
            return p;
        }

        /**
         * We will animate this view when it appears on the screen.
         * @param pos
         */
        public void animate(int pos) {
            /**if(pos > last_postition){//if havent animated
                cardView.animate().setInterpolator(new AnticipateOvershootInterpolator()).setDuration(1000).rotation(360).start();
                last_postition = pos;
            }*/
        }
    }
}