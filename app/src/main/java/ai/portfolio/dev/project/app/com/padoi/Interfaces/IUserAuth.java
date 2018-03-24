package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.facebook.Profile;

/**
 * Created by gabe on 3/13/2018.
 */

public interface IUserAuth {
    void asyncFBUser(AccessToken accessToken);
    Profile getFBProfile(GraphResponse graphResponse);
}
