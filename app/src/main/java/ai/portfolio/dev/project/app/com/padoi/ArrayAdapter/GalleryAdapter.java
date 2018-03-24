package ai.portfolio.dev.project.app.com.padoi.ArrayAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import ai.portfolio.dev.project.app.com.padoi.R;

/**
 * Created by gabe on 3/14/2018.
 */

public class GalleryAdapter<T> extends ArrayAdapter<T> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    private T[] list;
    public GalleryAdapter(Context context, int resource, T[] list) {
        super(context, resource,list);
        this.list = list;
    }
    @Override
    public T getItem(int position) {
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
        View rowView = inflater.inflate(R.layout.gallery_listview_layout, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.galleryImageView);
        InputStream is = null;
        try {
            is = this.getContext().getAssets().open("images/"+list[position].toString());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
           // Log.d("IIIIIMAGE PATH: ",list[position].toString()+" is null? "+(bitmap==null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
