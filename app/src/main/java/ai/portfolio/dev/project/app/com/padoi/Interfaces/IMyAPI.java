package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import android.location.Location;
import android.support.v4.app.LoaderManager;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;

public interface IMyAPI {
    /**
     * Will fetch from firebase a list of Live bands near user Location
     * @param userLoc user geolocation
     * @return
     */
  LoaderManager.LoaderCallbacks<List<BandUser>> getNearbyLiveEvents(Location userLoc);
}
