package ai.portfolio.dev.project.app.com.padoi.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;

import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * Created by gabe on 3/13/2018.
 * This is a Asynchrouns Call to fetch a picture froma URL and load it to a given ImaveView when completed.
 */

public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
    private Context context;
    private ImageView imageView;
    private String folder, imagename;
    /**
     * Doesnt save the image just fetches the image from url and displays. No save.
     * @param imageView
     */
    public DownLoadImageTask(Context con, ImageView imageView){
            this.context=con;
            this.folder=null;
            this.imagename=null;
            this.imageView = imageView;
    }

    /**
     * Saves the image to a specified location with the specified name.
     * @param imageView
     * @param folder
     * @param imageName
     * @param con
     */
        public DownLoadImageTask(Context con, ImageView imageView, String folder, String imageName){
            this.imagename = imageName;
            this.folder = folder;
            this.imageView = imageView;
            this.context = con;
        }
        @Override
        protected Bitmap doInBackground(String...urls){
            try {
                String urlOfImage = urls[0];
                java.net.URL url = new java.net.URL(urlOfImage);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                input.close();
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap result){
            if(folder!=null && result!=null){
                PADOI.saveToInternalSorage(context,result,folder,imagename);
            }
            if(result!=null && imageView!=null){ //set imageView without saving if there is a a image to put
                imageView.setImageBitmap(result);
            }
        }
    }