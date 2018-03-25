package ai.portfolio.dev.project.app.com.padoi.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.Models.PADOIUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.RecycleViewAdpaters.HorizontalAdapter;

/**
 * Created by gabe on 3/17/2018.
 */

public class LiveBandTask extends AsyncTask<String,Void,String> {
    private Context context;
    private RecyclerView rv;
    private PADOIUser user;
    public LiveBandTask(PADOIUser user, Context context, RecyclerView rv){
        this.rv = rv;
        this.context = context;
        this.user = user;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param strings The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try{
           HttpResponse response = Unirest.get("https://padoi-bdbd.restdb.io/rest/mock-data")
                    .header("x-apikey", "555d0d7ec8b4f29d51bceb1660e9402e84eda")
                    .header("cache-control", "no-cache")
                    .asString();
           return response.getBody().toString();//JSON OBJECT
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    private String TAG="REST API::";

    /**
     * Update Listview with the data
     * @param result
     */
    protected void onPostExecute(String result){

       /* Intent intent = new Intent(PADOI.HTTP_RESPONSE_LIVE_BANDS);
        intent.setAction(PADOI.HTTP_RESPONSE_LIVE_BANDS);
        intent.putExtra(PADOI.HTTP_RESPONSE_LIVE_BANDS, result);
        // broadcast the completion
       // Log.d(TAG, "RESULT = " + result);

        this.context.sendBroadcast(intent);*/

        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<BandUser>>(){}.getType();
        List<BandUser> list = gson.fromJson(result, founderListType);

        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(user,list, context, R.layout.trending_layout);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        //RecyclerView horizontal_recycler_view = (RecyclerView)rootView.findViewById(R.id.recycle_view_BANDS);
        rv.setLayoutManager(horizontalLayoutManager);
        rv.setAdapter(horizontalAdapter);
        //rv.setAdapter(new BandUserAdapter(context, R.layout.band_user_layout,list));
    }

}
