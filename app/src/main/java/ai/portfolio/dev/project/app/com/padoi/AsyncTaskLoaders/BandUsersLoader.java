package ai.portfolio.dev.project.app.com.padoi.AsyncTaskLoaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;

/**
 * Created by gabe on 3/28/2018.
 */

public class BandUsersLoader extends AsyncTaskLoader<List<BandUser>> {

    // We hold a reference to the Loader’s data here.
    private List<BandUser> mData;

    public BandUsersLoader(Context context) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(context);
    }

    @Override
    public List<BandUser> loadInBackground() {
        try {
            HttpResponse response = Unirest.get("https://padoi-bdbd.restdb.io/rest/mock-data")
                    .header("x-apikey", "555d0d7ec8b4f29d51bceb1660e9402e84eda")
                    .header("cache-control", "no-cache")
                    .asString();
            String result = response.getBody().toString();//JSON OBJECT
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<BandUser>>() {
            }.getType();
            List<BandUser> list = gson.fromJson(result, founderListType);
           return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<BandUser> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<BandUser> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
           /* if (mObserver == null) {
                mObserver = new SampleObserver();
                // TODO: register the observer
            }*/

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(broadcastReceiver);
        // The Loader is being reset, so we should stop monitoring for changes.
         if (broadcastReceiver != null) {
                // TODO: unregister the observer
             broadcastReceiver = null;
            }
    }

    private void releaseResources(List<BandUser> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/
    /*********************************************************************/

    // NOTE: Implementing an observer is outside the scope of this post (this example
    // uses a made-up "SampleObserver" to illustrate when/where the observer should
    // be initialized).

    // The observer could be anything so long as it is able to detect content changes
    // and report them to the loader with a call to onContentChanged(). For example,
    // if you were writing a Loader which loads a list of all installed applications
    // on the device, the observer could be a BroadcastReceiver that listens for the
    // ACTION_PACKAGE_ADDED intent, and calls onContentChanged() on the particular
    // Loader whenever the receiver detects that a new application has been installed.
    // Please don’t hesitate to leave a comment if you still find this confusing! :)
    //private SampleObserver mObserver;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };
}
