package ai.portfolio.dev.project.app.com.padoi.RecycleViewAdpaters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.Models.PADOIUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * This layout uses trending_layout.xml
 * This class is responsible to control all of the BANDUSER recycle view from MAINACTIVITY class. CONTROLLER on GUI layout of trending items.
 * Created by gabe on 3/23/2018.
 */

public class BandAdapterRV extends RecyclerView.Adapter<BandAdapterRV.MyViewHolder> {

    List<BandUser> horizontalList;
    Context context;
    int layout_id;
    PADOIUser currUser;

    public BandAdapterRV(PADOIUser user, List<BandUser> horizontalList, Context context, int layout_id) {
        this.horizontalList = horizontalList==null?new ArrayList<BandUser>():horizontalList;
        this.context = context;
        this.layout_id = layout_id;
        this.currUser  = user;
    }

    public void add(List<BandUser> data) {
        if(this.horizontalList==null)horizontalList=new ArrayList<>();
        horizontalList.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        if(horizontalList!=null)horizontalList.clear();
        notifyDataSetChanged();
    }

    public List<BandUser> getList() {
        return this.horizontalList;
    }

    /**
     * This class saves the VIEWS to display in recycle view. In this case we will use band_user_layout.xml
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        boolean isImageFitToScreen;// click to fullscreen image. not implemented yet
        LinearLayout details;
        ImageButton beAFanBtn;

        public MyViewHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.trendingImageView);
            txtview=(TextView) view.findViewById(R.id.band_name_pic_overlay);
            details = (LinearLayout)view.findViewById(R.id.popup_info_id);
            isImageFitToScreen = false;
            beAFanBtn = (ImageButton)view.findViewById(R.id.imageButton_be_a_fan);
        }

        public void toggleFullScreen() {

        }

        public void toggleBandInfo() {
            details.setVisibility(details.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
        }
        public boolean isFollowing(String bandID){
            if(currUser.getLikesBandID()==null)return false;
            return currUser.getLikesBandID().contains(bandID);
        }
        public void bandLikeDislikeClicked(String bandID){
            Toast.makeText(context,"Like button clicked!!",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BandUser band =horizontalList.get(position);
        Bitmap image = PADOI.loadImage(context,PADOI.FOLDER_USERS_IMAGES,band.getId());
        if(image==null) {
            new DownLoadImageTask(context, holder.imageView, PADOI.FOLDER_USERS_IMAGES, band.getId()).execute("https://picsum.photos/600/600/?random");
        }else {
            holder.imageView.setImageBitmap(image);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                   holder.toggleFullScreen();
                }

            });
        }
        holder.txtview.setText(band.getName());
        if(holder.isFollowing(band.getId())) {
            PADOI.userIsFollowingBand(context, holder.beAFanBtn, true);
        }
        holder.txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.toggleBandInfo();
            }
        });

        holder.beAFanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bandLikeDislikeClicked(band.getId());
                PADOI.userIsFollowingBand(context, holder.beAFanBtn, true);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }
}