package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.Models.Location;
import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;

/**
 * Created by gabe on 3/24/2018.
 */

public interface IFirebase {
    void fetchFirebaseUser(String fb_ID);
    void fetchFirebaseBandUsers(List<BandUser> bandUsers, int pagination);
    void fetchLiveBands(Location from, int radius);
    void fetchAllLiveBands();
    void createNewUser(PadoiUser fb_user, android.location.Location loc);
}
