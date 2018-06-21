package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gabe on 3/13/2018.
 */

public interface ISplashScreen {
    void continueToApp(FirebaseUser prof);
    void login();
}
