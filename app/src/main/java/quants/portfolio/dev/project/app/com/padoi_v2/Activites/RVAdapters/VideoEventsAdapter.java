package quants.portfolio.dev.project.app.com.padoi_v2.Activites.RVAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Activites.TEST.TEST;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class VideoEventsAdapter extends RecyclerView.Adapter<VideoEventsAdapter.ViewHolder> {
    private ArrayList<String> mData;
    public VideoEventsAdapter(Context context) {
        mData = new ArrayList<String>(TEST.generateTestString(20));//display user uploads
    }

    @NonNull
    @Override
    public VideoEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_video_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoEventsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv.setText(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        VideoView vv;
        public ViewHolder(@NonNull View view) {
            super(view);
            tv = view.findViewById(R.id.textView);
            vv = view.findViewById(R.id.videoView);

        }
    }
}
