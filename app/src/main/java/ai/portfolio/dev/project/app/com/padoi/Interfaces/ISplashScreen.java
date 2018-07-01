package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import android.animation.ObjectAnimator;
import android.widget.ImageView;

import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;

/**
 * This interface provides a control structure for a splash screen
 */

public interface ISplashScreen {
    void continueToApp(PadoiUser prof);
    void login();
    ObjectAnimator animateSplash(ImageView iv);
}
