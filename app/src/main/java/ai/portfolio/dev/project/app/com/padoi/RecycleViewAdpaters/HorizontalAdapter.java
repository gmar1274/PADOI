package ai.portfolio.dev.project.app.com.padoi.RecycleViewAdpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * This layout uses trending_layout.xml
 * Created by gabe on 3/23/2018.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    List<BandUser> horizontalList;
    Context context;
    int layout_id;

    public HorizontalAdapter(List<BandUser> horizontalList, Context context,int layout_id) {
        this.horizontalList = horizontalList;
        this.context = context;
        this.layout_id = layout_id;
    }

    /**
     * This class saves the VIEWS to display in recycle view. In this case we will use band_user_layout.xml
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;

        public MyViewHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            txtview=(TextView) view.findViewById(R.id.bandUser_TextView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BandUser band =horizontalList.get(position);
        new DownLoadImageTask(context, holder.imageView, PADOI.FOLDER_USERS_IMAGES,band.getId()).execute("https://picsum.photos/400/300/?random");
        holder.txtview.setText(band.getName());
       /* holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //String list = horizontalList.get(position).txt.toString();
                //Toast.makeText(MainActivity.this, list, Toast.LENGTH_SHORT).show();
            }

        });*/

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }
}