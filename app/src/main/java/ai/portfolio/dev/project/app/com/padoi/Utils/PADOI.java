package ai.portfolio.dev.project.app.com.padoi.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by gabe on 3/14/2018.
 */

public class PADOI {
    public static final int WIDTH=300,HEIGHT=300;//url image fb
    public static final String HTTP_RESPONSE_LIVE_BANDS="LiveBandAPI";
    public static final String FOLDER_USERS_IMAGES = "USER_IMAGES";

    /**
     * DEPRACTED DO NOT USE...https://stackoverflow.com/questions/29188127/android-attempted-to-serialize-forgot-to-register-a-type-adapter
     * Need to implement  implements JsonSerializer<CLASSOBJ>... and use gson builder to register it....best to use for now:::
     *
     *  Gson gson = new Gson();
     Type founderListType = new TypeToken<ArrayList<BandUser>>(){}.getType();
     List<BandUser> list = gson.fromJson(result, founderListType);

     * This utility class will generate a List<T> of the json objects in a format of list from a json array response.
     * @param json
     *
     * @param <T>
     * @return
     */
   /* public static <T>List<T> toList(String json, Class<T> type) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<T>>(){}.getType();
        List<T> type_list = gson.fromJson(json, founderListType);
        return type_list;
       api key: 8449881-b84baea387c13b5f92e741f70
    }*/

    /**
     * This method takes a context, folder, and imageName as a parameter to load image from disk. Otherwise return null if not there.
     * @param context
     * @param folder
     * @param imageName
     * @return
     */
   public static Bitmap loadImage(Context context,String folder ,String imageName){
       try {
           ContextWrapper cw = new ContextWrapper(context);
           File path1 = cw.getDir(folder, Context.MODE_PRIVATE);
           File f = new File(path1, imageName);
           Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
           return b;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       return null;
   }

    /**
     * This method takes a context, bitmap, folder, and imagename as a parameter and returns true if saved successfully. False if otherwise.
     * @param context - Context for ContextWrapper.
     * @param bitmapImage - Bitmap to be saved.
     * @param folder - folder directory name.
     * @param imageName - image name.
     * @return
     */
    public static boolean saveToInternalSorage(Context context, Bitmap bitmapImage, String folder ,String imageName) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(folder, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            //Editor editor = sharedpreferences.edit();
            //editor.putString("saved", "na");
            //editor.commit();
            Log.d("IMAGE::: ","IMAGE SUCCESS SAVED!!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns Mime type from url string.
     * @param url
     * @return
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
