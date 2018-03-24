package ai.portfolio.dev.project.app.com.padoi.ArrayAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * Created by gabe on 3/19/2018.
 */

public class BandUserAdapter extends ArrayAdapter<BandUser> {
    private static final String DEBUG_PIC_URL = "https://picsum.photos/200/300/?random";
    private List<BandUser>list;
        public BandUserAdapter(Context context, int resource,List<BandUser> list) {
            super(context, resource,list);
            this.list = list;
        }
        @Override
        public BandUser getItem(int position) {
            return super.getItem(position);
        }

        /**
         * Load images from assetmanager
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.band_user_layout, parent, false);
            ImageView imageView = (ImageView)new ImageView(getContext()) ;//rowView.findViewById(R.id.bandUser_imageView);
            TextView name = (TextView)rowView.findViewById(R.id.bandUser_TextView);
            BandUser band = getItem(position);
            name.setText(band.getName());
            TextView loc = (TextView)rowView.findViewById(R.id.location_tv);
            loc.setText(band.toString());
            try {
                Bitmap image = PADOI.loadImage(getContext(), PADOI.FOLDER_USERS_IMAGES,band.getId());
                if(image == null){ // NO IMAGED IS PRESENT. Fetch image using async.
                    new DownLoadImageTask(this.getContext(),imageView).execute(DEBUG_PIC_URL);
                }else {
                    imageView.setImageBitmap(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rowView;
        }
        public int getSize(){
            return  this.list.size();
        }
    }
